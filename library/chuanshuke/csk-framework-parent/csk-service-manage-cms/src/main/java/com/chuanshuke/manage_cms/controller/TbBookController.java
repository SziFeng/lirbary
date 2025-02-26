package com.chuanshuke.manage_cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chuanshuke.api.cms.book_manage.BookManageControllerApi;
import com.chuanshuke.framework.domain.cms.TbBook;
import com.chuanshuke.framework.domain.cms.TbBookDesc;
import com.chuanshuke.framework.domain.cms.request.QueryBookRequest;
import com.chuanshuke.framework.domain.cms.response.CmsBookResult;
import com.chuanshuke.framework.domain.cms.response.CmsImageResult;
import com.chuanshuke.framework.model.response.CommonCode;
import com.chuanshuke.framework.model.response.QueryResponseResult;
import com.chuanshuke.framework.model.response.ResponseResult;
import com.chuanshuke.framework.utils.FastDFSClient;
import com.chuanshuke.manage_cms.service.TbBookService;

@RestController
@RequestMapping("/cms/book")
public class TbBookController implements BookManageControllerApi {

	@Value("${chuanshuke.fastdfs.imageServerUrl}")
	private String IMAGE_SERVER_URL;

	@Autowired
	TbBookService tbBookService;

	@GetMapping("/list/{page}/{size}")
	@Override
	public QueryResponseResult<TbBook> findList(@PathVariable("page") int page,
			@PathVariable("size")int size, QueryBookRequest queryBookRequest) {

		return tbBookService.findAll(page, size, queryBookRequest);
	}

	@PostMapping("/add")
	@Override
	public CmsBookResult add(@RequestBody TbBook tbBook) {
		return tbBookService.add(tbBook);
	}

	@Override
	@GetMapping("/get/{id}")
	public TbBook findById(@PathVariable Long id) {
		return tbBookService.getById(id);
	}

	@PostMapping("/edit/{id}")
	@Override
	public CmsBookResult edit(@PathVariable Long id, @RequestBody TbBook tbBook) {

		return tbBookService.update(id, tbBook);
	}

	@PostMapping("/delete/{id}")
	@Override
	public ResponseResult delete(@PathVariable Long id) {

		return tbBookService.delete(id);

	}

	@PostMapping("/add/desc/{id}")
	@Override
	public ResponseResult addDesc(@PathVariable(name = "id") Long bookId, @RequestBody TbBookDesc tbBookDesc) {

		return tbBookService.addBookDesc(bookId, tbBookDesc);
	}

	@GetMapping("/get/desc/{id}")
	@Override
	public TbBookDesc findDescByBookId(@PathVariable(name = "id") Long bookId) {
		return tbBookService.findDescByBookId(bookId);
	}

	@PostMapping("/edit/desc/{id}")
	@Override
	public ResponseResult editBookDesc(@PathVariable Long bookId,  @RequestBody TbBookDesc tbBookDesc) {
		return tbBookService.updateBookDesc(bookId, tbBookDesc);
	}

	@PostMapping("/pic/upload")
	@Override
	public CmsImageResult uploadPic(MultipartFile uploadFile) {
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//fastdfs图片存储地址
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//拼接完整
			url = IMAGE_SERVER_URL+url;
			return new CmsImageResult(CommonCode.SUCCESS, url);
		} catch (Exception e) {
			e.printStackTrace();
			return new CmsImageResult(CommonCode.FAIL, null);
		}

	}

}
