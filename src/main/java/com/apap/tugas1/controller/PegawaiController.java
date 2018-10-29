package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> listJabatan = jabatanService.getJabatanList();
		model.addAttribute("listJabatan", listJabatan);
		List<InstansiModel> listInstansi = instansiService.getListInstansi();
		model.addAttribute("listInstansi", listInstansi);
	
		return "home-sipeg";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String detailPegawai(@RequestParam (value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		List<JabatanModel> jabatanPegawai = pegawai.getJabatanList();
		double gaji = pegawaiService.generateGaji(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("jabatanPegawai", jabatanPegawai);
		model.addAttribute("gaji", (int)gaji);
		
		return "detail-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tertua-termuda", method = RequestMethod.GET)
	private String viewPegawaiTermudaTertua(@RequestParam ("id") long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiDetailById(id);

		PegawaiModel pegawaiTertua = pegawaiService.getPegawaiTertua(instansi.getListPegawai());
		PegawaiModel pegawaiTermuda = pegawaiService.getPegawaiTermuda(instansi.getListPegawai());
		
		model.addAttribute("pegawaiTertua", pegawaiTertua);
		model.addAttribute("pegawaiTermuda", pegawaiTermuda);
		
		return "view-pegawai-tua-muda";
	}
	
	@RequestMapping(value = "/provinsi/instansi", method = RequestMethod.GET)
	 public @ResponseBody
	 List<InstansiModel> findInstansi(@RequestParam(value = "idProvinsi", required = true) long idProvinsi) {
	 ProvinsiModel provinsi = provinsiService.getProvinsiDetailById(idProvinsi);
	 List<InstansiModel> listInstansi = provinsi.getInstansiList();
	  
	 // Change listPegawai and provinsi to null to prevent error while converting to JSON
	 for(int i = 0; i < listInstansi.size(); i++) {
		listInstansi.get(i).setListPegawai(null);
		listInstansi.get(i).setProvinsi(null);
	}
	 	return listInstansi;
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		List<JabatanModel> jabatanPegawai = new ArrayList<>();
	
		pegawai.setJabatanList(jabatanPegawai);
		model.addAttribute("pegawai", pegawai);

		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
	  
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
	  
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params = {"save"})
	private String addPegawai(@ModelAttribute PegawaiModel pegawai, @RequestParam(value = "idProvinsi") long idProvinsi, Model model) {
		System.out.println(pegawai.getNama());
		System.out.println(pegawai.getInstansi());
		// Set instansi from instansi Id
		InstansiModel instansiTemp = instansiService.getInstansiDetailById(pegawai.getInstansi().getId());
		pegawai.setInstansi(instansiTemp);
		
		System.out.println(pegawai.getInstansi().getNama());
	  
		// Set NIP by generating
		String nipTemp = pegawaiService.generateNip(pegawai);
		pegawai.setNip(nipTemp);
		
		System.out.println(pegawai.getNip());
	  
		List<JabatanModel> listJabatanTemp = pegawai.getJabatanList();
		JabatanModel jabatanTemp;
		long idTemp;
		for(int i = 0; i < listJabatanTemp.size(); i++) {
			idTemp = listJabatanTemp.get(i).getId();
			jabatanTemp = jabatanService.getJabatanDetailById(idTemp);
			listJabatanTemp.set(i, jabatanTemp);
	  }
	  pegawai.setJabatanList(listJabatanTemp);
	  
	  System.out.println(pegawai.getJabatanList().get(0).getNama());
	  
	  pegawaiService.addPegawai(pegawai);
	  model.addAttribute("msg", "Pegawai dengan nama " + pegawai.getNama() + " berhasil ditambah dengan NIP " + pegawai.getNip());
	  return "success";
	 }
	
	@RequestMapping(value= "/pegawai/cari", method=RequestMethod.GET)
	private String cariPegawaiSubmit(
			@RequestParam(value="idProvinsi", required = false) String idProvinsi,
			@RequestParam(value="idInstansi", required = false) String idInstansi,
			@RequestParam(value="idJabatan", required = false) String idJabatan,
			Model model) {
		
		
		
		model.addAttribute("listProvinsi", provinsiService.getListProvinsi());
		model.addAttribute("listInstansi", instansiService.getListInstansi());
		model.addAttribute("listJabatan", jabatanService.getJabatanList());
		List<PegawaiModel> listPegawai = pegawaiService.getListPegawai();
		
		if ((idProvinsi==null || idProvinsi.equals("")) && (idInstansi==null||idInstansi.equals("")) && (idJabatan==null||idJabatan.equals(""))) {
			return "searchPegawai";
		}
		else {
			if (idProvinsi!=null && !idProvinsi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getProvinsi().getId()).toString().equals(idProvinsi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idProvinsi", Long.parseLong(idProvinsi));
			}
			else {
				model.addAttribute("idProvinsi", "");
			}
			if (idInstansi!=null&&!idInstansi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getId()).toString().equals(idInstansi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idInstansi", Long.parseLong(idInstansi));
			}
			else {
				model.addAttribute("idInstansi", "");
			}
			if (idJabatan!=null&&!idJabatan.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					for (JabatanModel jabatan:peg.getJabatanList()) {
						if (((Long)jabatan.getId()).toString().equals(idJabatan)) {
							temp.add(peg);
							break;
						}
					}
					
				}
				listPegawai = temp;
				model.addAttribute("idJabatan", Long.parseLong(idJabatan));
			}
			else {
				model.addAttribute("idJabatan", "");
			}
		}
		model.addAttribute("listPegawai",listPegawai);
		return "view-pegawai";
		
	}
	
}
