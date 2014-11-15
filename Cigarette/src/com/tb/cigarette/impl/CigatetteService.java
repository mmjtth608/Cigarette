package com.tb.cigarette.impl;

import java.util.ArrayList;
import java.util.List;

import com.tb.cigarette.model.Cigarette;

public interface CigatetteService {
	/**
	 * 插入一条香烟数据
	 * 
	 * @param cigarette
	 */
	void insertCigarette(Cigarette cigarette);

	/**
	 * 插入多条香烟数据
	 * 
	 * @param cigarettes
	 */
	void insertCigarettes(List<Cigarette> cigarettes);

	/**
	 * 更新该条香烟数据
	 * 
	 * @param cigarette
	 */
	void updateCigarette(Cigarette cigarette);

	/**
	 * 删除该条香烟数据
	 * 
	 * @param cigarette
	 */
	void deleteCigarette(Cigarette cigarette);

	/**
	 * 加载所有的香烟
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadAllCigarette();

	/**
	 * 加载某一产地的香烟
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadChandiCigarette();

	/**
	 * 加载某一厂家的香烟
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadChangjiaCigarette();

	/**
	 * 加载某关键字的香烟
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadKeyCigarette(String key);
}
