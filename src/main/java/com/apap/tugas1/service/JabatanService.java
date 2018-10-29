package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

public interface JabatanService {
	public void addJabatan(JabatanModel jabatan);
	
	public JabatanModel getJabatanDetailById(long id);
	
	public List<JabatanModel> getJabatanList();
	
	public void deleteJabatan(long id);
	
	JabatanDb viewAllJabatan();

	List<JabatanModel> getAllJabatan();
}
