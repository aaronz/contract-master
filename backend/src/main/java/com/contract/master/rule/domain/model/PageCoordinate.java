package com.contract.master.rule.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PageCoordinate {

    private int page;
    private List<Rect> rects;

    public PageCoordinate() {
        this.rects = new ArrayList<>();
    }

    public PageCoordinate(int page, List<Rect> rects) {
        this.page = page;
        this.rects = rects != null ? rects : new ArrayList<>();
    }

    public static PageCoordinate of(int page, List<Rect> rects) {
        return new PageCoordinate(page, rects);
    }

    public static PageCoordinate empty() {
        return new PageCoordinate();
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public List<Rect> getRects() { return rects; }
    public void setRects(List<Rect> rects) { this.rects = rects; }
    public void addRect(Rect rect) {
        this.rects.add(rect);
    }

    public static class Rect {
        private double x;
        private double y;
        private double width;
        private double height;

        public Rect() {}

        public Rect(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        public double getWidth() { return width; }
        public void setWidth(double width) { this.width = width; }
        public double getHeight() { return height; }
        public void setHeight(double height) { this.height = height; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Rect rect = (Rect) o;
            return Double.compare(rect.x, x) == 0 &&
                   Double.compare(rect.y, y) == 0 &&
                   Double.compare(rect.width, width) == 0 &&
                   Double.compare(rect.height, height) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, width, height);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageCoordinate that = (PageCoordinate) o;
        return page == that.page && Objects.equals(rects, that.rects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, rects);
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"page\":").append(page);
        json.append(",\"rects\":[");
        for (int i = 0; i < rects.size(); i++) {
            if (i > 0) json.append(",");
            Rect r = rects.get(i);
            json.append("{\"x\":").append(r.x);
            json.append(",\"y\":").append(r.y);
            json.append(",\"width\":").append(r.width);
            json.append(",\"height\":").append(r.height).append("}");
        }
        json.append("]}");
        return json.toString();
    }
}
