package com.twotoasters.sectioncursoradaptersample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.twotoasters.sectioncursoradapter.adapter.SectionCursorAdapter;
import com.twotoasters.sectioncursoradapter.adapter.viewholder.SViewHolder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SectionCursorAdapterTest {

    private Context context;

    private static final SortedMap<Integer, Object> SECTION_MAP;
    private static final SortedMap<Integer, Object> SECTION_MAP_ALT;
    private static final Object[] FAST_SCROLL_OBJECTS;

    static {
        SECTION_MAP = new TreeMap<Integer, Object>();
        SECTION_MAP.put(0, "A");
        SECTION_MAP.put(3, "B");
        SECTION_MAP.put(6, "C");

        SECTION_MAP_ALT = new TreeMap<Integer, Object>();
        SECTION_MAP_ALT.put(2, "A");
        SECTION_MAP_ALT.put(4, "B");
        SECTION_MAP_ALT.put(5, "C");

        FAST_SCROLL_OBJECTS = new Object[3];
        FAST_SCROLL_OBJECTS[0] = "A";
        FAST_SCROLL_OBJECTS[1] = "B";
        FAST_SCROLL_OBJECTS[2] = "C";
    }

    @Mock Cursor cursor;
    private TestAdapter adapter;
    private TestAdapter spyAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(cursor.getPosition()).thenReturn(0);
        when(cursor.getCount()).thenReturn(10);

        context = Robolectric.application;
        adapter = new TestAdapter(context, cursor, 0);
        spyAdapter = spy(adapter);
    }

    @Test
    public void itShouldCheckIfSection() {
        adapter.setSections(new TreeMap<>(SECTION_MAP));
        assertThat(adapter.isSection(0)).isTrue();
        assertThat(adapter.isSection(1)).isFalse();
        assertThat(adapter.isSection(2)).isFalse();
        assertThat(adapter.isSection(3)).isTrue();
        assertThat(adapter.isSection(4)).isFalse();
        assertThat(adapter.isSection(5)).isFalse();
        assertThat(adapter.isSection(6)).isTrue();
        assertThat(adapter.isSection(7)).isFalse();
    }

    @Test
    public void itShouldGetIndexWithinSections() {
        adapter.setSections(new TreeMap<>(SECTION_MAP));
        assertThat(adapter.getSectionPosition(0)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(1)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(2)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(3)).isEqualTo(1);
        assertThat(adapter.getSectionPosition(4)).isEqualTo(1);
        assertThat(adapter.getSectionPosition(5)).isEqualTo(1);
        assertThat(adapter.getSectionPosition(6)).isEqualTo(2);
        assertThat(adapter.getSectionPosition(7)).isEqualTo(2);

        adapter.setSections(new TreeMap<>(SECTION_MAP_ALT));
        assertThat(adapter.getSectionPosition(0)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(1)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(2)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(3)).isEqualTo(0);
        assertThat(adapter.getSectionPosition(4)).isEqualTo(1);
        assertThat(adapter.getSectionPosition(5)).isEqualTo(2);
        assertThat(adapter.getSectionPosition(6)).isEqualTo(2);
    }

    @Test
    public void itShouldGetListPositionWithoutSections() {
        adapter.setSections(new TreeMap<>(SECTION_MAP));
        assertThat(adapter.getCursorPositionWithoutSections(0)).isEqualTo(SectionCursorAdapter.NO_CURSOR_POSITION);
        assertThat(adapter.getCursorPositionWithoutSections(1)).isEqualTo(0);
        assertThat(adapter.getCursorPositionWithoutSections(2)).isEqualTo(1);
        assertThat(adapter.getCursorPositionWithoutSections(3)).isEqualTo(SectionCursorAdapter.NO_CURSOR_POSITION);
        assertThat(adapter.getCursorPositionWithoutSections(4)).isEqualTo(2);
        assertThat(adapter.getCursorPositionWithoutSections(5)).isEqualTo(3);
        assertThat(adapter.getCursorPositionWithoutSections(6)).isEqualTo(SectionCursorAdapter.NO_CURSOR_POSITION);
        assertThat(adapter.getCursorPositionWithoutSections(7)).isEqualTo(4);

        adapter.setSections(new TreeMap<>(SECTION_MAP_ALT));
        assertThat(adapter.getCursorPositionWithoutSections(0)).isEqualTo(0);
        assertThat(adapter.getCursorPositionWithoutSections(1)).isEqualTo(1);
        assertThat(adapter.getCursorPositionWithoutSections(2)).isEqualTo(SectionCursorAdapter.NO_CURSOR_POSITION);
        assertThat(adapter.getCursorPositionWithoutSections(3)).isEqualTo(2);
        assertThat(adapter.getCursorPositionWithoutSections(4)).isEqualTo(SectionCursorAdapter.NO_CURSOR_POSITION);
        assertThat(adapter.getCursorPositionWithoutSections(5)).isEqualTo(SectionCursorAdapter.NO_CURSOR_POSITION);
        assertThat(adapter.getCursorPositionWithoutSections(6)).isEqualTo(3);
        assertThat(adapter.getCursorPositionWithoutSections(7)).isEqualTo(4);

        adapter.setSections(new TreeMap<Integer, Object>());
        assertThat(adapter.getCursorPositionWithoutSections(0)).isEqualTo(0);
        assertThat(adapter.getCursorPositionWithoutSections(1)).isEqualTo(1);
        assertThat(adapter.getCursorPositionWithoutSections(2)).isEqualTo(2);
    }

    // ********** Fast Scroll SectionIndexer Tests **********/

    @Test
    public void itShouldGetPositionForSection() {
        adapter.setSections(new TreeMap<Integer, Object>(SECTION_MAP));
        assertThat(adapter.getPositionForSection(0)).isEqualTo(0);
        assertThat(adapter.getPositionForSection(1)).isEqualTo(3);
        assertThat(adapter.getPositionForSection(2)).isEqualTo(6);

        adapter.setSections(new TreeMap<Integer, Object>(SECTION_MAP_ALT));
        assertThat(adapter.getPositionForSection(0)).isEqualTo(2);
        assertThat(adapter.getPositionForSection(1)).isEqualTo(4);
        assertThat(adapter.getPositionForSection(2)).isEqualTo(5);
    }

    @Test
    public void itShouldGetSectionForPosition() {
        adapter.setSections(new TreeMap<Integer, Object>(SECTION_MAP));
        assertThat(adapter.getSectionForPosition(0)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(1)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(2)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(3)).isEqualTo(1);
        assertThat(adapter.getSectionForPosition(4)).isEqualTo(1);
        assertThat(adapter.getSectionForPosition(5)).isEqualTo(1);
        assertThat(adapter.getSectionForPosition(6)).isEqualTo(2);
        assertThat(adapter.getSectionForPosition(7)).isEqualTo(2);

        adapter.setSections(new TreeMap<Integer, Object>(SECTION_MAP_ALT));
        assertThat(adapter.getSectionForPosition(0)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(1)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(2)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(3)).isEqualTo(0);
        assertThat(adapter.getSectionForPosition(4)).isEqualTo(1);
        assertThat(adapter.getSectionForPosition(5)).isEqualTo(2);
        assertThat(adapter.getSectionForPosition(6)).isEqualTo(2);
        assertThat(adapter.getSectionForPosition(7)).isEqualTo(2);
    }

    @Test
    public void itShouldGetFastScrollSections() {
        Object[] fastScrollObjects;

        adapter.setSections(new TreeMap<Integer, Object>(SECTION_MAP));
        fastScrollObjects = adapter.getSections();
        assertThat(fastScrollObjects).isNotNull();
        assertThat(fastScrollObjects.length).isEqualTo(3);
        assertThat(fastScrollObjects[0]).isEqualTo("A");
        assertThat(fastScrollObjects[1]).isEqualTo("B");
        assertThat(fastScrollObjects[2]).isEqualTo("C");

        adapter.setSections(new TreeMap<Integer, Object>(SECTION_MAP_ALT));
        fastScrollObjects = adapter.getSections();
        assertThat(fastScrollObjects).isNotNull();
        assertThat(fastScrollObjects.length).isEqualTo(3);
        assertThat(fastScrollObjects[0]).isEqualTo("A");
        assertThat(fastScrollObjects[1]).isEqualTo("B");
        assertThat(fastScrollObjects[2]).isEqualTo("C");
    }

    private static class TestAdapter extends SectionCursorAdapter<Object, TestViewHolder, TestViewHolder> {

        public SortedMap<Integer, Object> sections;

        private TestAdapter(Context context, Cursor cursor, int flags) {
            // Not testing views.
            super(context, cursor, flags, 0, 0);
        }

        @Override
        protected View onNewSectionView(ViewGroup parent) {
            return null;
        }

        @Override
        protected TestViewHolder onCreateSectionViewHolder(View view, ViewGroup parent) {
            return new TestViewHolder(view);
        }

        @Override
        protected void onBindSectionViewHolder(TestViewHolder holder, int position, Object section) {

        }

        @Override
        protected View onNewItemView(ViewGroup parent) {
            return null;
        }

        @Override
        protected TestViewHolder onCreateItemViewHolder(View view, ViewGroup parent, int viewType) {
            return new TestViewHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(TestViewHolder holder, Cursor cursor) {

        }

        @Override
        protected SortedMap<Integer, Object> buildSections(Cursor cursor) {
            return this.sections;
        }

        @Override
        protected Object getSectionFromCursor(Cursor cursor) {
            return null;
        }

        public void setSections(SortedMap<Integer, Object> sections) {
            this.sections = sections;
            notifyDataSetChanged();
        }
    }

    public static class TestViewHolder extends SViewHolder {

        public TestViewHolder(View itemView) {
            super(itemView);
        }
    }
}