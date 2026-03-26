package com.ring.welkin.common.core.page;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ListPaging<T> {

	private int pageNum = IPageable.DEFAULT_PAGENUM;
	private int pageSize = IPageable.DEFAULT_PAGESIZE;
	private int totalPage;

	private final List<T> list;

	public static <E> ListPaging<E> of(List<E> list, int pageSize) {
		return new ListPaging<E>(list, pageSize);
	}

	public static <E> ListPaging<E> of(List<E> list) {
		return new ListPaging<E>(list, IPageable.DEFAULT_PAGESIZE);
	}

	private ListPaging(List<T> list, int pageSize) {
		this.list = list;
		this.pageSize = getPageSize(pageSize);
		this.totalPage = getTotalPage(pageSize);
	}

	public IPage<T> page(int pageNum) {
		if (list == null || list.isEmpty()) {
			return IPage.<T>of(pageNum, pageSize, list.size(), new ArrayList<T>());
		}
		this.pageNum = getPageNum(pageNum, totalPage);
		int fromIndex = (this.pageNum - 1) * pageSize;
		int toIndex = (this.pageNum - 1) * pageSize + pageSize;
		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		return IPage.<T>of(this.pageNum, pageSize, list.size(), list.subList(fromIndex, toIndex));
	}

	public IPage<T> pageByOffset(int offset) {
		return page(getPageNum(offset / pageSize, totalPage));
	}

	public int getPageNum(int pageNum, int totalPage) {
		if (pageNum <= 0) {
			pageNum = 1;
		}
		if (pageNum > totalPage) {
			pageNum = totalPage;
		}
		return pageNum;
	}

	private int getPageSize(int pageSize) {
		if (pageSize <= 0) {
			pageSize = 10;
		}
		return pageSize;
	}

	private int getTotalPage(int pageSize) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		return (int) Math.ceil((double) list.size() / getPageSize(pageSize));
	}

	// public static void main(String[] args) {
	// List<Integer> list = Arrays.asList(0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
	// 22, 23, 24, 25, 26, 27, 28, 29, 30);
	// ListPaging<Integer> listPaging = new ListPaging<Integer>(list, 11);
	//
	// IPage<Integer> page = null;
	// int totalPage = listPaging.getTotalPage();
	// for (int i = 1; i <= totalPage; i++) {
	// page = listPaging.page(i);
	// System.out.println("page" + listPaging.getPageNum() + ": " + page.getList());
	// }
	// }

}
