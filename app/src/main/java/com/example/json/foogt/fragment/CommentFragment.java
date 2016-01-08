package com.example.json.foogt.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.json.foogt.R;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.entity.UserInfoMsg;
import com.example.json.foogt.entity.msgRelation;
import com.example.json.foogt.entity.msgRelation1;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.alibaba.fastjson.JSON.parse;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER_ID = "userId";
    private int userId;
    private List<msgRelation> m;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            m= (List<msgRelation>) msg.obj;
            LogUtil.d("wang",m.size()+"");

            int g=0;
            List<HashMap<String,String>> l=new ArrayList<>();
            System.out.println("gggg" + g);
            LogUtil.d("m",m.get(0).getContent1());
            for (g=0;g<m.size();g++){
                HashMap<String,String> h=new HashMap<>();
                h.put("name1",m.get(g).getName1());
                h.put("content1","@"+m.get(g).getContent1());
                h.put("name2",m.get(g).getName2());
                h.put("content2",m.get(g).getContent2());
                l.add(h);
            }



            SimpleAdapter adapter=new SimpleAdapter(getActivity(),l,R.layout.item,
                    new String[]{"name1","content1","name2","content2"},
                    new int[]{R.id.textView,R.id.textView4,R.id.textView3,R.id.textView5});
            lv.setAdapter(adapter);
        }
    };



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
/*    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    public static CommentFragment newInstance(int userId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            userId = getArguments().getInt(ARG_USER_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comment, container, false);
        LogUtil.d("comment", "今天是个好日子");
        lv= (ListView) v.findViewById(R.id.listView2);

        String url = IConst.SERVER_ADDR + "ShowComment";
        String data = "userId=" + userId;
        LogUtil.d("data", data);
        HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //json传递list
                m = JSON.parseObject(response, new TypeReference<List<msgRelation>>() {
                });
                LogUtil.d("xixi", m.size() + "");
                Message msg = new Message();
                msg.obj = m;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        return v;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
