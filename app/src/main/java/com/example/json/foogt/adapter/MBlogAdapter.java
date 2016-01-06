package com.example.json.foogt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.json.foogt.R;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.IConst;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mzz on 2015/12/28.
 */
public class MBlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MBlogAdapter";

    private ArrayList<BlogInfo> list;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean haveMoreBlog = true;
    private OnItemClickListener mOnItemClickListener;
    private ImageLoader mImageLoader;

    /**
     * \
     * init
     *
     * @param list        data to show
     * @param listener    handle click event, like collection, comment, repost. null if don't handle click event
     * @param imageLoader to load user head img from server
     */
    public MBlogAdapter(ArrayList<BlogInfo> list, OnItemClickListener listener, ImageLoader imageLoader) {
        super();
        this.list = list;
        mOnItemClickListener = listener;
        mImageLoader = imageLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return a blog item view
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_blog, parent, false);
            // TODO: 2015/12/28 what if parent is null , how to measure
            return new MBlogAdapter.ViewHolder(v, mOnItemClickListener);
        } else {
            // return a "loading" view
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //bind data to view
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).username.setText(list.get(position).getUsername());
            ((ViewHolder) holder).postTime.setText(new SimpleDateFormat("MM.dd hh:mm").format(list.get(position).getPostTime()));
            ((ViewHolder) holder).msg.setText(list.get(position).getMsg());
            // TODO: 2016/1/4 img change by msg state
            ((ViewHolder) holder).headIMG.setDefaultImageResId(R.drawable.picture);
            ((ViewHolder) holder).headIMG.setErrorImageResId(R.drawable.search);
            if (mImageLoader != null) {
                ((ViewHolder) holder).headIMG.setImageUrl(IConst.SERVLET_ADDR + "GetHeadIMG?uid=" + list.get(position).getAuthorId(), mImageLoader);
            }
        }
    }

    @Override
    public int getItemCount() {
        // if have more blogs, total size = list.size()+1 to show Loading footer.
        //else, no need to show "loading"
        int begin = haveMoreBlog ? 1 : 0;
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
        public Button repost, comment;
        public NetworkImageView headIMG;

        public OnItemClickListener mOnClickListener;
        // TODO: 2015/12/28 headImg

        // TODO: 2016/1/4 figure out a better way , not constructor
        //init need a listener to handle click event
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
            headIMG = (NetworkImageView) itemView.findViewById(R.id.img_blog_user_profile);

            collect.setOnClickListener(this);
            repost.setOnClickListener(this);
            comment.setOnClickListener(this);
        }

        //handle click event. listener is defined on init.
        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            if (mOnClickListener != null) {
                if (v instanceof ImageView) {
                    //click collect
                    mOnClickListener.onCollectClick(list.get(itemPosition).getMsgId());
                } else if (v.getId() == R.id.btn_comment) {
                    //click comment
                    mOnClickListener.onCommentClick(list.get(itemPosition));
                } else if (v.getId() == R.id.btn_repost) {
                    //click repost
                    mOnClickListener.onRepostClick(list.get(itemPosition));
                }
            }
        }
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * according to position to return itemViewType.
     * itemViewType is used to decide which view to return in onCreateViewHolder
     *
     * @param position item position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (haveMoreBlog && position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public boolean isHaveMoreBlog() {
        return haveMoreBlog;
    }

    public void setHaveMoreBlog(boolean haveMoreBlog) {
        this.haveMoreBlog = haveMoreBlog;
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
