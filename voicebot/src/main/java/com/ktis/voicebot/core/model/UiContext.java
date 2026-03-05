package com.ktis.voicebot.core.model;

import java.util.ArrayList;
import java.util.List;

public class UiContext {

    private String screenId;
    private Integer focusIndex;
    private String lastAction;

    private List<ContentItem> lastContentList = new ArrayList<ContentItem>();

    public String getScreenId() { return screenId; }
    public void setScreenId(String screenId) { this.screenId = screenId; }

    public Integer getFocusIndex() { return focusIndex; }
    public void setFocusIndex(Integer focusIndex) { this.focusIndex = focusIndex; }

    public String getLastAction() { return lastAction; }
    public void setLastAction(String lastAction) { this.lastAction = lastAction; }

    public List<ContentItem> getLastContentList() { return lastContentList; }
    public void setLastContentList(List<ContentItem> lastContentList) { this.lastContentList = lastContentList; }

    public static class ContentItem {
        private String id;
        private String title;

        public ContentItem() {}

        public ContentItem(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }
}