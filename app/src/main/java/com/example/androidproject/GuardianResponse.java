package com.example.androidproject;

import androidx.annotation.NonNull;
import java.util.List;

public class GuardianResponse
{
    private String status;
    private String userTier;
    private int total;
    private int startIndex;
    private int pageSize;
    private int currentPage;
    private int pages;
    private String orderBy;
    private List<GuardianResult> results;

    public GuardianResponse(String status, String userTier, int total, int startIndex, int pageSize, int currentPage, int pages, String orderBy, List<GuardianResult> results) {
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pages = pages;
        this.orderBy = orderBy;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<GuardianResult> getResults() {
        return results;
    }

    public void setResults(List<GuardianResult> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public String toString() {
        return "GuardianResponse{" +
                "status='" + status + '\'' +
                ", userTier='" + userTier + '\'' +
                ", total=" + total +
                ", startIndex=" + startIndex +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", pages=" + pages +
                ", orderBy='" + orderBy + '\'' +
                ", results=" + results +
                '}';
    }
}
