package com.app.raghu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.raghu.model.Student;
import com.app.raghu.service.StudentService;
import com.app.raghu.util.StudentUtil;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentService service;
	
	
	@PostMapping("/register")
	public ResponseEntity<?> saveStudent(@RequestBody Student student) {
		ResponseEntity<?> response=null;
		try {
			Integer stdId=service.saveStudent(student);
			response=new ResponseEntity<String>(stdId+"-Inserted", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> showOneStudents(@PathVariable Integer id) {
		ResponseEntity<?> response=null;
		boolean exist=service.studentExist(id);
		if(exist) {
			Student s=service.getStudentById(id);
			response=new ResponseEntity<Student>(s, HttpStatus.OK);
		}else {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return response;
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> showAllStudents() {
		ResponseEntity<?> response=null;
		List<Student> students=service.getAllStudents();
		if(students!=null && !students.isEmpty()) {
			response=new ResponseEntity<List<Student>>(students, HttpStatus.OK);
		}else {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return response;
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
		ResponseEntity<?> response=null;
		boolean exist=service.studentExist(id);
		if(exist) {
			service.deleteStudent(id);
			response=new ResponseEntity<String>(id+"-Removed", HttpStatus.OK);
		}else {
			response=new ResponseEntity<String>("STUDENT NOT FOUND",HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<?> editStudent(@PathVariable Integer id,@RequestBody Student student) {
		ResponseEntity<?> response=null;
		boolean exist=service.studentExist(id);
		if(exist) {
			Student source=service.getStudentById(id);
			StudentUtil.mapNotNullValues(source,student);
			service.saveStudent(source);
			response=new ResponseEntity<String>(id+"-Updated", HttpStatus.OK);
		}else {
			response=new ResponseEntity<String>("STUDENT NOT FOUND",HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}