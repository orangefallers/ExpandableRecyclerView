# ExpandableRecyclerView
[ ![Download](https://api.bintray.com/packages/orangefallers/maven/ExpandableRecyclerView/images/download.svg?version=1.0.2) ](https://bintray.com/orangefallers/maven/ExpandableRecyclerView/1.0.2/link)


---
# 效果展示
![image](https://github.com/orangefallers/ExpandableRecyclerViewDemo/blob/master/expandablerecyclerview/expandable_domo.gif)

---
1.如何引用
```java
implementation 'com.ofcat.expandablerecyclerview:expandablerecyclerview:1.0.2'
```

---
2.XML 使用 ExpandalbeRecyclerView
```xml
 <com.ofcat.expandablerecyclerview.ExpandableRecyclerView
        android:id="@+id/expandableRecycler"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
 ```
 ---
 3.實作 ExpandableRecyclerAdapter
 ```java
 public class CustomExpandableRecyclerAdapter extends ExpandableRecyclerAdapter {
    @Override 
    public int getGroupCount() {
        return 0;
    }

    @Override 
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public ViewHolderGroup onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override 
    public ViewHolderChild onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override 
    public void onBindGroupViewHolder(ViewHolderGroup holder, int groupPosition, boolean isExpanded) {

    }

    @Override 
    public void onBindChildViewHolder(ViewHolderChild holder, int groupPosition, int childPosition, boolean isLastChild) {

    }
}
```
---
4.實作 自定義的GroupViewHolder ChildViewHolder
```java
class DemoViewGroupHolder extends ViewHolderGroup {

    public DemoViewGroupHolder(View itemView) {
        super(itemView);
    }

}

class DemoViewChildHolder extends ViewHolderChild {

    public DemoViewChildHolder(View itemView) {
        super(itemView);
    }

}
```
---
5.ExpandableRecyclerView SetAdapter
```java
ExpandableRecycylerView expandableRecyclerView = findViewById(R.id.expandableRecycler);
expandableRecyclerView.setAdapter(new CustomExpandableRecyclerAdapter());
```

