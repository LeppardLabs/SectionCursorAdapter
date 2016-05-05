# SectionCursorAdapter [![Run Status](https://api.shippable.com/projects/572b5e982a8192902e1f1467/badge?branch=master)](https://app.shippable.com/projects/572b5e982a8192902e1f1467)
SectionCursorAdapter adds sections and fast scroll to CursorAdapter as an easily implementable feature.
A blog post on the implementation rational can be found on [toastdroid.com](http://toastdroid.com/2014/05/09/adding-sections-to-cursoradapter)

## 3.0.0 Branch
This is the 3.0 Branch. This is currently a W.I.P

## Changelog

- TODO

![sections](screenshots/sections.png)      ![dialog](screenshots/dialog.png)

## Download

If you are using maven add to your pom file:

```groovy
dependencies {
    compile 'com.twotoasters.SectionCursorAdapter:library:3.0.0'
}
```

## Basics - Concepts are good but view stuff is out of date.
SectionCursorAdapter is implemented in a similar way to Android's CursorAdapter which it extends. Instead of having `newView` and `bindView` the SectionCursorAdpater uses `newSectionView` and `bindSectionView` plus `newItemView` and `bindItemView`. 

There is one additional abstract method `Object getSectionFromCursor(Cursor cursor)`. This is the method tells the adapter how to remap the cursor positions to allow for sections. The object which is returned is then passed through to `newSectionView` and `bindSectionView`. You can make an alphabitical adapter with the following method. Noob tip: You will have to sort alphabitically when querying your database for your cursor.

```java
@Override
protected Object getSectionFromCursor(Cursor cursor) {
    int columnIndex = cursor.getColumnIndex(StoreModel.NAME);
    String name = cursor.getString(columnIndex);
    return name.toUpperCase().substring(0, 1);
}
```

If you prefer to use the onItemClickListenter instead of using click listeners when binding your views you'll more then likely need to convert your `position` to a `cursorPosition`.

```java
listView.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyAdapter adapter = (MyAdapter) parent.getAdapter();
        Object sectionObject = adapter.getItem(position);
        int cursorPosition = adapter.getCursorPositionWithoutSections(position);

        if (adapter.isSection(position) && sectionObject != null) {
            // Handle the section being clicked on.
            Log.i("SCA", "Section: " + sectionObject);
        } else if (cursorPosition != SectionCursorAdapter.NO_CURSOR_POSITION) {
            // Handle the cursor item being clicked on.
            Log.i("SCA", "CursorPosition: " + cursorPosition);
        }
    }
});
```
## Advanced
To build sections in a more advanced way you can override `buildSections`. The following is an example for how to build a simple alphabitical map.
```java
@Override
protected SortedMap<Integer, Object> buildSections(Cursor cursor) {
    TreeMap<Integer, Object> sections = new TreeMap<>();
    int columnIndex = cursor.getColumnIndex(StoreModel.NAME);
    int cursorPosition = 0;
        
    while (cursor.moveToNext()) {
        String name = cursor.getString(columnIndex);
        String section = name.toUpperCase().substring(0, 1);
        if (!sections.containsValue(section)) {
            sections.put(cursorPosition + sections.size(), section);
        }
        cursorPosition++;
    }
    return sections;
}
```
You can give a custom object as a value in the map instead of a number or string. To use the fast scroll with this object override `toString`. This will allow you to control what is displayed in the fast scroll dialog. Note that in versions of Android before KitKat this dialog does not resize to fit content. SectionCursorAdapter by default only allows a maximum of 3 characters in this dialog on these older version of Android, but by overriding `getMaxIndexerLength()` the length can be whatever you choose.

## License

    Copyright 2016 Ticketmaster
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
       
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
