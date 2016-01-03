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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.R;
import com.example.json.foogt.adapter.MBlogAdapter;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to show Focus Blogs.
 * <p/>
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_USER_ID = "userId";
    private static final String TAG = "HomeFragment";
    private int userId;
    private RecyclerView rv;
    private SwipeRefreshLayout sw;
    private MBlogAdapter adapter;
    private ArrayList<BlogInfo> list;
    private RequestQueue mQueue;
    private int currentPage;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId UserId of current User
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(int userId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
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
        }
        mQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rv = (RecyclerView) v.findViewById(R.id.rv_home_msg);
        sw = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe);

        sw.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MBlogAdapter(list);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView停止拖动而且已经到达了最后一个item，执行自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 == adapter.getItemCount()) {
                        load(currentPage);
                    }
                }
            }
        });
        onRefresh();
        return v;
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        load(currentPage);
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

    @Override
    public void onResponse(String response) {
        List<BlogInfo> results = JSON.parseObject(response, new TypeReference<List<BlogInfo>>() {
        });
        LogUtil.i(TAG, "results size" + results.size());
        if (results.size() > 0) {
            currentPage++;
            list.addAll(results);
            LogUtil.d(TAG, "list size" + list.size());
        } else {
            adapter.setHaveMoreBlogs(false);
        }
        adapter.notifyDataSetChanged();
        hideProgress();
        //auto load more, until "Loading" TAG is not visible on screen or no more blog to load.
        //if LastVisibleItemPosition == LastCompletelyVisibleItemPosition,"Loading" TAG may visible.
        if (layoutManager.findLastVisibleItemPosition() == layoutManager.findLastCompletelyVisibleItemPosition()) {
            load(currentPage);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgress();
        Toast.makeText(getContext(), R.string.http_fail, Toast.LENGTH_SHORT).show();
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

    public void load(final int page) {
        LogUtil.d(TAG, "Loading Page " + page);
        if (page == 0) {
            showProgress();
            list.clear();
            adapter.setHaveMoreBlogs(true);
        }
        if (!adapter.isHaveMoreBlogs()) {
            return;
        }
        String url = IConst.SERVLET_ADDR + "GetBlogs?userId=" + userId + "&page=" + page;
        LogUtil.d(TAG, url);
        StringRequest stringRequest = new StringRequest(url, this, this);
        mQueue.add(stringRequest);
    }

    private void showProgress() {
        sw.setRefreshing(true);
    }

    private void hideProgress() {
        sw.setRefreshing(false);
    }
}
