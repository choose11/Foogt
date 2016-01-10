# Report

��ǩ���ո�ָ����� δ����

---

## Bug��

- û��д4.4���ϵ�URI������
- ���Ե�ʱ����������ȡ����URI��file��ͷ�ģ�ò����û����ӽ������������ݿ⣬����Query��ȡpath��ʱ�򷵻�ֵ��null��������һ��������ͺ��ˡ�

---

1. `Button.setClickable(false)`��Ч
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

3. `dos2unix` ����ת�����з���ʽ [�ο�](http://kelvinh.github.io/blog/2012/03/23/recursively-list-directories-and-process-files-in-shell/)
ֱ��ͨ�� find ������� -exec ѡ�������� xargs ������ʵ�֣������û��������һ�����еķ������£����Ժܺõش����ļ��ո񣩣�
 ```
find ~/.vim -type f -print0 | xargs -0 dos2unix
 ```

4. GIT����

 ʹ���Լ��ķ�֧���п�����develop���кϲ����롣
 ```
git fetch //��ȡԶ�����з�֧
git checkout develop
git merge origin/develop 
git checkout myBranch
git rebase develop //���Լ�����commit�ŵ�develop����󣬱�����ʷ������������ͻ�Ժ���Ҫ git add -u ,git rebase --continue
git log //ȷ��develop�����commit�Ѿ����ϲ����Լ���commit��ʷ�У��Լ�����commit������commit��
git checkout develop
git merge --no-ff myBranch
git push origin develop
 ```
5. [oracle ��ҳ Ч��](http://blog.csdn.net/sfdev/article/details/2801712)
 > ע�⣺��ROWNUM��Ϊ��ѯ����ʱ��������order by֮ǰִ�У�����Ҫ�ر�С�ģ�
�����������ѯTABLE1�а�TABLE1_ID�������е�ǰ10����¼���������µ�SQL����ɣ�
 `SELECT * FROM TABLE1 WHERE ROWNUM <= 10 ORDER BY TABLE1_ID DESC;`

 [Ƕ���Ӳ�ѯ�в��������order by ���](http://blog.sina.com.cn/s/blog_62e7fe6701015154.html)
 > ������֤����Ƕ���Ӳ�ѯ�в��������order by ��䡣���磺
`select * from scott.emp
where ename in (select ename from scott.emp order by ename)`
�ᱨ ��ORA-00907:ȱ�������š��Ĵ���
> 
����������Ƕ���Ӳ�ѯ�ٰ�װһ�㣬��Ϊ�ڶ����ѯ��������ͼ��
`select * from scott.emp
where ename in (select * from(select ename from scott.emp order by ename))`
����Գɹ�ִ�С�

6. ToolBar���졣 [���](http://guides.codepath.com/android/Using-the-App-ToolBar#overview)
����ʹ��`NoActionBar`��
������ʹ��`android.support.v7.widget.Toolbar`��
 ``` 
// Find the toolbar view inside the activity layout
Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
// Sets the Toolbar to act as the ActionBar for this Activity window.
// Make sure the toolbar exists in the activity and is not null
setSupportActionBar(toolbar); 
 ```
֮��Ϳ��Լ�������ActionBar���޸�ToolBar��
ʹ��`android.support.design.widget.CoordinatorLayout`��`android.support.design.widget.AppBarLayout`�����ܻ����¼����������`app:layout_behavior="@string/appbar_scrolling_view_behavior"��`ToolBar�Ĳ�������`app:layout_scrollFlags="scroll|enterAlways"`����ʵ��[��������ToolBar](https://github.com/mzgreen/HideOnScrollExample)��
[app:layout_scrollFlags���Խ���](http://guides.codepath.com/android/Handling-Scrolls-with-CoordinatorLayout#expanding-and-collapsing-toolbars)��
��������FloatButton��Ҫ�Լ�д���롣ָ��`app:layout_behavior="com.example.json.foogt.util.ScrollingFABBehavior`���ο�[��������ToolBar](https://github.com/mzgreen/HideOnScrollExample)��
ToolBar�ĸ߶Ⱥͱ�����ɫ��Ҫ��`?attr/`�����á�
������һ��ҳ��ļ�ͷ��ɫ�޸�Ϊ��ɫ����Ҫ���������`colorControlNormal`���ԣ������������`AppTheme`��������ԵĻ����`EditText`���������ɫ�ĵ��������Լ��½�һ��Style��
7. ���ü�������ͼƬ�ο���[����](http://blog.csdn.net/guolin_blog/article/details/17482165)�Ĳ��ͣ�ʹ��Volley���أ��л��档
8. RecyclerView��֧��`SetOnItemClickListener`�����Լ���ʵ������Adapter�Ĺ��캯���д���Listener�����ڲ����á���Fragment�ж���Listener��*����ʵ�ֵ��е��ª����֪����û�и��߼���ʵ�֡�*
9. һ���Զ����ظ����С���ɡ���Ǹ�Ҳ��������ˡ���ôд����ʵ��û�и������ݵ�ʱ����ʾ"���ڼ���"��
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
10. ����RecyclerView��Item֮��ָ��ߵ�С���ɡ�http://stackoverflow.com/a/31227010/3819519
Ŀǰ�е�СBUG��Item��������һ����ʱ�򣬱����ǻ�ɫ�ġ�Ӧ�ÿ���ͨ�����ø߶Ƚ������
������RecyclerView�ı���ɫʱ��Item�ı���ɫ�̳С���Ҫͨ��`?android:colorBackground`�Ļ�Ĭ�ϱ�����ɫ��[�ο�](http://stackoverflow.com/questions/19841640/android-default-apptheme-background-colors)��
11. �ϴ�ͷ��
[Servlet](http://haolloyin.blog.51cto.com/1177454/368162/)
Client ʹ��Post��������MenuActivity��

    