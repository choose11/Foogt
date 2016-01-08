package com.example.json.foogt.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.R;
import com.example.json.foogt.activity.CommentBlogActivity;
import com.example.json.foogt.activity.TransferActivity;
import com.example.json.foogt.adapter.MBlogAdapter;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.BitmapCache;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment to show Focus Blogs.
 * <p/>
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements
        Response.ErrorListener,
        SwipeRefreshLayout.OnRefreshListener,
        MBlogAdapter.OnItemClickListener {
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_FRIEND_ID = "friendId";
    private static final String ARG_TYPE = "type";
    private static final String TAG = "HomeFragment";
    public static final int HOME = 0;
    public static final int COLLECTION = 1;
    public static final int PEOPLEBLOG = 2;

    private int userId;
    private int friendId;
    private String loadBaseUrl;
    private int currentPage;
    private int lastVisibleItem;
    private int type;

    private RecyclerView rv;
    private SwipeRefreshLayout sw;
    private MBlogAdapter adapter;
    private ArrayList<BlogInfo> list;
    private RequestQueue mQueue;
    private LinearLayoutManager layoutManager;
    private ImageLoader imageLoader;
    private BitmapCache bitmapCache;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId UserId of current User
     * @param type   if this fragment is init for home or collection activity
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(int userId, int type) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(int userId, int friendId, int type) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        System.out.println("userId = " + userId);
        args.putInt(ARG_FRIEND_ID, friendId);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
            System.out.println("HomeFragmentuserId = " + userId);
            friendId = getArguments().getInt(ARG_FRIEND_ID);
            System.out.println("friendId = " + friendId);
            type = getArguments().getInt(ARG_TYPE);
            //according to fragment type to define base url.
            // base url is used for load data from server
            if (userId != -1) {
                if (type == HOME) {
                    loadBaseUrl = IConst.SERVLET_ADDR + "GetBlogs";
                } else if (type == COLLECTION) {
                    loadBaseUrl = IConst.SERVLET_ADDR + "GetCollections";
                } else if (type == PEOPLEBLOG) {
                    loadBaseUrl = IConst.SERVER_ADDR + "GetUserOwnBlogs";
                }
            } else {
                loadBaseUrl = IConst.SERVER_ADDR + "GetHotBlogs";
            }

        }
        //volley request queue
        mQueue = Volley.newRequestQueue(getContext());
        bitmapCache = BitmapCache.getInstance();
        //user head img loader. use bitmap cache
        imageLoader = new ImageLoader(mQueue, bitmapCache);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
//        CommentFragment.actionStart(getActivity(),msg,userId);

        rv = (RecyclerView) v.findViewById(R.id.rv_home_msg);
        sw = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe);

        sw.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        if (type == HOME) {
            adapter = new MBlogAdapter(list, this, imageLoader);
        } else if (type == COLLECTION) {
            adapter = new MBlogAdapter(list, null, imageLoader);
        } else {
            adapter = new MBlogAdapter(list, null, imageLoader);

        }
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView停止拖动而且已经到达了最后一个item，执行自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 == adapter.getItemCount()) {

                        if (type == PEOPLEBLOG) {
                            peopleLoad(currentPage);
                        } else {
                            load(currentPage);
                        }
                    }
                }
            }
        });
        //first time. load data.
        onRefresh();
        return v;
    }


    /**
     * refresh data.
     * set current page = 0
     */
    @Override
    public void onRefresh() {
        currentPage = 0;
        bitmapCache.clear();
        if (type == PEOPLEBLOG) {
            peopleLoad(currentPage);
        } else {
            load(currentPage);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // net error listener
    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgress();
        Toast.makeText(getContext(), R.string.http_fail, Toast.LENGTH_SHORT).show();
    }

    // implementation of MBlogAdapter.OnItemClickListener
    @Override
    public void onCollectClick(BlogInfo msg) {
        if (userId != -1) {
            collectBlog(msg);
        } else {
            Toast.makeText(getActivity(), "请侧拉出菜单，进行登陆！！", Toast.LENGTH_SHORT).show();
        }
    }

    // implementation of MBlogAdapter.OnItemClickListener
    @Override
    public void onCommentClick(BlogInfo msg) {
        LogUtil.d(TAG, "onCommentClick");
        if (userId != -1) {
            for (BlogInfo b : list) {
                LogUtil.d(TAG, "BlogInfo" + b.getAuthorId());
            }
            CommentBlogActivity.actionStart(getActivity(), msg, userId);
        } else {
            Toast.makeText(getActivity(), "请侧拉出菜单，进行登陆！！", Toast.LENGTH_SHORT).show();
        }
    }

    // implementation of MBlogAdapter.OnItemClickListener
    @Override
    public void onRepostClick(BlogInfo msg) {
        LogUtil.d(TAG, "onRepostClick");
        if (userId != -1) {
            for (BlogInfo b : list) {
                LogUtil.d(TAG, "BlogInfo" + b.getAuthorId());
            }
            System.out.println(msg.getAuthorId());
            if (userId == msg.getAuthorId()) {
                Toast.makeText(getActivity(), "自己转发自己的微博？你是不是傻？", Toast.LENGTH_LONG).show();
            } else {
                TransferActivity.actionStart(getActivity(), msg, userId);
            }
        } else {
            Toast.makeText(getActivity(), "请侧拉出菜单，进行登陆！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    //load data from server
    public void load(final int page) {
        LogUtil.d(TAG, "Loading Page " + page);
        if (page == 0) {
            showProgress();
            list.clear();
            adapter.setHaveMoreBlog(true);
        }
        if (!adapter.isHaveMoreBlog()) {
            return;
        }
        if (userId != -1) {
            String url = loadBaseUrl + "?userId=" + userId + "&page=" + page;
            LogUtil.d(TAG, url);
            //volley request
            StringRequest stringRequest = new StringRequest(url, new GetBlogsListener(), this);
            mQueue.add(stringRequest);
        } else {
            String url = loadBaseUrl + "?page=" + page;
            LogUtil.d(TAG, url);
            //volley request
            StringRequest stringRequest = new StringRequest(url, new GetBlogsListener(), this);
            mQueue.add(stringRequest);
        }
    }


    //collect blog. send data to server

    public void peopleLoad(final int page) {
        LogUtil.d(TAG, "Loading Page " + page);
        if (page == 0) {
            showProgress();
            list.clear();
            adapter.setHaveMoreBlog(true);
        }
        if (!adapter.isHaveMoreBlog()) {
            return;
        }
        String url = loadBaseUrl + "?userId=" + friendId + "&page=" + page;
        LogUtil.d(TAG, url);
        StringRequest stringRequest = new StringRequest(url, new GetBlogsListener(), this);
        mQueue.add(stringRequest);
    }

    private void collectBlog(BlogInfo msg) {
        int msgId = msg.getMsgId();
        LogUtil.d(TAG, "msgID=" + msg.getMsgId());
        String url = IConst.SERVLET_ADDR + "Collection?uid=" + userId + "&msgId=" + msgId;
        StringRequest stringRequest = new StringRequest(url, new CollectListener(msg), this);
        mQueue.add(stringRequest);
    }

    //show refresh progress
    private void showProgress() {
        sw.setRefreshing(true);
    }

    private void hideProgress() {
        sw.setRefreshing(false);
    }

    //on get blog data from server
    public class GetBlogsListener implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            List<BlogInfo> results = JSON.parseObject(response, new TypeReference<List<BlogInfo>>() {
            });
            LogUtil.i(TAG, "results size" + results.size());
            if (results.size() > 0) {
                currentPage++;
                list.addAll(results);
                Collections.sort(list);
                LogUtil.d(TAG, "list size" + list.size());
            } else {
                currentPage++;
                adapter.setHaveMoreBlog(false);
            }
            adapter.notifyDataSetChanged();
            hideProgress();
            //auto load more, until "Loading" TAG is not visible on screen or no more blog to load.
            //if LastVisibleItemPosition == LastCompletelyVisibleItemPosition,"Loading" TAG may visible.
            if (layoutManager.findLastVisibleItemPosition() == layoutManager.findLastCompletelyVisibleItemPosition()) {
                load(currentPage);
            }
        }
    }

    //handle server response.
    private class CollectListener implements Response.Listener<String> {
        private BlogInfo msg;

        public CollectListener(BlogInfo msg) {
            this.msg = msg;
        }

        @Override
        public void onResponse(String response) {
            boolean result = Utility.handleBooleanResultResponse(response);
            if (result) {
                msg.setCollected(true);
                adapter.notifyDataSetChanged();
            }
            Toast.makeText(getContext(), result ? "Success" : "Failed", Toast.LENGTH_SHORT).show();
        }
    }


}
