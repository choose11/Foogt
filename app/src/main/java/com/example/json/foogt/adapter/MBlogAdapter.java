package com.example.json.foogt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.json.foogt.R;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mzz on 2015/12/28.
 */
public class MBlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG ="MBlogAdapter";

    private ArrayList<BlogInfo> list;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean haveMoreBlogs = true;
    private OnItemClickListener mOnItemClickListener;

    public MBlogAdapter(ArrayList<BlogInfo> list, OnItemClickListener listener) {
        super();
        this.list = list;
        mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_blog, parent, false);
            // TODO: 2015/12/28 what if parent is null , how to measure
            return new MBlogAdapter.ViewHolder(v, mOnItemClickListener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).username.setText(list.get(position).getUsername());
            ((ViewHolder) holder).postTime.setText(new SimpleDateFormat("MM.dd hh:mm").format(list.get(position).getPostTime()));
            ((ViewHolder) holder).msg.setText(list.get(position).getMsg());
            // TODO: 2016/1/4 img change by msg state
        }
    }

    @Override
    public int getItemCount() {
        // if have more blogs, total size = list.size()+1 to show Loading footer.
        int begin = haveMoreBlogs ? 1 : 0;
        if (list == null) {
            return begin;
        }
        return list.size() + begin;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView username;
        public TextView postTime;
        public TextView msg;
        public ImageView collect;
        public Button repost,comment;
        public OnItemClickListener mOnClickListener;
        // TODO: 2015/12/28 headImg

        // TODO: 2016/1/4 figure out a better way , not constructor
        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            // TODO: 2015/12/28 complete
            mOnClickListener = listener;
            username = (TextView) itemView.findViewById(R.id.txt_blog_name);
            postTime = (TextView) itemView.findViewById(R.id.txt_blog_time);
            msg = (TextView) itemView.findViewById(R.id.txt_blog_msg);
            repost = (Button) itemView.findViewById(R.id.btn_repost);
            comment = (Button) itemView.findViewById(R.id.btn_comment);
            collect = (ImageView) itemView.findViewById(R.id.img_collection);

            collect.setOnClickListener(this);
            repost.setOnClickListener(this);
            comment.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            if (v instanceof ImageView) {
                if (mOnClickListener != null) {
                    mOnClickListener.onCollectClick(list.get(itemPosition).getMsgId());
                }
            } else if (v.getId() == R.id.btn_comment) {
                if (mOnClickListener != null) {
                    mOnClickListener.onCommentClick(list.get(itemPosition));
                }
            } else if (v.getId() == R.id.btn_repost) {

            }
        }
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (haveMoreBlogs && position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public boolean isHaveMoreBlogs() {
        return haveMoreBlogs;
    }

    public void setHaveMoreBlogs(boolean haveMoreBlogs) {
        this.haveMoreBlogs = haveMoreBlogs;
    }

    public interface OnItemClickListener {
        void onCollectClick(int msgId);

        void onCommentClick(BlogInfo msg);

        void onRepostClick(BlogInfo msg);
    }

    // TODO: 2016/1/4 abandon
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
