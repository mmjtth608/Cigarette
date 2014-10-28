package com.tb.cigarette.impl;

import java.util.ArrayList;
import java.util.List;

import com.tb.cigarette.model.Cigarette;

public interface CigatetteService {
	/**
	 * ����һ����������
	 * 
	 * @param cigarette
	 */
	void insertCigarette(Cigarette cigarette);

	/**
	 * ���������������
	 * 
	 * @param cigarettes
	 */
	void insertCigarettes(List<Cigarette> cigarettes);

	/**
	 * ���¸�����������
	 * 
	 * @param cigarette
	 */
	void updateCigarette(Cigarette cigarette);

	/**
	 * ɾ��������������
	 * 
	 * @param cigarette
	 */
	void deleteCigarette(Cigarette cigarette);

	/**
	 * �������е�����
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadAllCigarette();

	/**
	 * ����ĳһ���ص�����
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadChandiCigarette();

	/**
	 * ����ĳһ���ҵ�����
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadChangjiaCigarette();

	/**
	 * ����ĳ�ؼ��ֵ�����
	 * 
	 * @return
	 */
	ArrayList<Cigarette> loadKeyCigarette(String key);
}
