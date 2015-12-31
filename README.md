# Report

��ǩ���ո�ָ����� δ����

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
//Ҫ��Ҫɾ���Լ��ķ�֧Ȼ�����develop������commit�ؽ���������
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
