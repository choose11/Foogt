# Report

标签（空格分隔）： 未分类

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
//要不要删掉自己的分支然后基于develop的最新commit重建，待测试
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
