# Report

标签（空格分隔）： 未分类

---

## Bug：

- 没有写4.4以上的URI解析。
- 测试的时候相册里面获取到的URI是file开头的，貌似是没有添加进相册的索引数据库，所以Query获取path的时候返回值是null。重启了一下虚拟机就好了。

---

1. `Button.setClickable(false)`无效
set OnClickListener on Button will make the button clickable, even `Btn.setClickable(false)`.

2. RecyclerList.Adapter.ViewHolder
while get view, we should use 
 ```
LayoutInflater.from(context).inflate(R.layout.x, parent, false);
 ```
we shoule **give the inflater a parent ViewGroup to supply LayoutParams**. otherwise can't get view measured correctly.
Actually 
 ```
View.inflate(getContext(),R.layout.x,container);
 ```
 would call **LayoutInflater's inflate()**

3. `dos2unix` 批量转换换行符格式 [参考](http://kelvinh.github.io/blog/2012/03/23/recursively-list-directories-and-process-files-in-shell/)
直接通过 find 命令加上 -exec 选项或者配合 xargs 命令来实现，简单易用还不会出错，一个可行的方案如下（可以很好地处理文件空格）：
 ```
find ~/.vim -type f -print0 | xargs -0 dos2unix
 ```

4. GIT操作

 使用自己的分支进行开发。develop进行合并代码。
 ```
git fetch //获取远程所有分支
git checkout develop
git merge origin/develop 
git checkout myBranch
git rebase develop //将自己的新commit放到develop的最后，保持历史清晰，解决完冲突以后需要 git add -u ,git rebase --continue
git log //确认develop里面的commit已经被合并到自己的commit历史中，自己的新commit是最新commit。
git checkout develop
git merge --no-ff myBranch
git push origin develop
 ```
5. [oracle 分页 效率](http://blog.csdn.net/sfdev/article/details/2801712)
 > 注意：当ROWNUM作为查询条件时，他是在order by之前执行，所以要特别小心；
比如我们想查询TABLE1中按TABLE1_ID倒序排列的前10条记录不能用如下的SQL来完成：
 `SELECT * FROM TABLE1 WHERE ROWNUM <= 10 ORDER BY TABLE1_ID DESC;`

 [嵌套子查询中不允许出现order by 语句](http://blog.sina.com.cn/s/blog_62e7fe6701015154.html)
 > 经测试证明在嵌套子查询中不允许出现order by 语句。例如：
`select * from scott.emp
where ename in (select ename from scott.emp order by ename)`
会报 “ORA-00907:缺少右括号”的错误。
> 
如果将上面的嵌套子查询再包装一层，成为第二层查询的内联视图。
`select * from scott.emp
where ename in (select * from(select ename from scott.emp order by ename))`
则可以成功执行。

6. ToolBar改造。 [简介](http://guides.codepath.com/android/Using-the-App-ToolBar#overview)
主题使用`NoActionBar`。
布局中使用`android.support.v7.widget.Toolbar`。
 ``` 
// Find the toolbar view inside the activity layout
Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
// Sets the Toolbar to act as the ActionBar for this Activity window.
// Make sure the toolbar exists in the activity and is not null
setSupportActionBar(toolbar); 
 ```
之后就可以继续操作ActionBar来修改ToolBar。
使用`android.support.design.widget.CoordinatorLayout`和`android.support.design.widget.AppBarLayout`。接受滑动事件的组件设置`app:layout_behavior="@string/appbar_scrolling_view_behavior"。`ToolBar的布局设置`app:layout_scrollFlags="scroll|enterAlways"`可以实现[滑动隐藏ToolBar](https://github.com/mzgreen/HideOnScrollExample)。
[app:layout_scrollFlags属性介绍](http://guides.codepath.com/android/Handling-Scrolls-with-CoordinatorLayout#expanding-and-collapsing-toolbars)。
滑动隐藏FloatButton需要自己写代码。指定`app:layout_behavior="com.example.json.foogt.util.ScrollingFABBehavior`。参考[滑动隐藏ToolBar](https://github.com/mzgreen/HideOnScrollExample)。
ToolBar的高度和背景颜色需要用`?attr/`来设置。
返回上一级页面的箭头颜色修改为白色，需要设置主题的`colorControlNormal`属性，但是如果设置`AppTheme`的这个属性的话会把`EditText`的输入框颜色改掉，所以自己新建一个Style。
7. 设置加载网络图片参考了[郭霖](http://blog.csdn.net/guolin_blog/article/details/17482165)的博客，使用Volley加载，有缓存。
8. RecyclerView不支持`SetOnItemClickListener`。我自己的实现是在Adapter的构造函数中传入Listener，在内部调用。在Fragment中定义Listener。*好像实现的有点丑陋，不知道有没有更高级的实现。*
9. 一个自动加载更多的小技巧。抱歉找不到出处了。这么写可以实现没有更多数据的时候不显示"正在加载"。
 ```
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

 ```
10. 设置RecyclerView中Item之间分割线的小技巧。http://stackoverflow.com/a/31227010/3819519
目前有点小BUG，Item数量不足一屏的时候，背景是灰色的。应该可以通过设置高度解决掉。
设置了RecyclerView的背景色时，Item的背景色继承。需要通过`?android:colorBackground`改回默认背景颜色。[参考](http://stackoverflow.com/questions/19841640/android-default-apptheme-background-colors)。
11. 上传头像。
[Servlet](http://haolloyin.blog.51cto.com/1177454/368162/)
Client 使用Post方法，见MenuActivity。

    