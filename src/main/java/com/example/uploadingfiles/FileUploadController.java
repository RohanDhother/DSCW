package com.example.uploadingfiles;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
// import com.example.uploadingfiles.GRPCServerServices;
//These were module i required to run the program

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.uploadingfiles.storage.StorageFileNotFoundException;
import com.example.uploadingfiles.storage.StorageService;

@Controller
public class FileUploadController {

	// GRPCServerServices GrpcServerServices;
	private final StorageService storageService;
	ArrayList<String> files = new ArrayList<String>(2);
	ArrayList<String[]> matrix1 = new ArrayList<String[]>();
	ArrayList<String[]> matrix2 = new ArrayList<String[]>();
	//Creates an arraylist to store each of the matrixs that were uploaded
	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {//This sets up the initial page and loads the upload html page

		model.addAttribute("files", storageService.loadAll().map(
		path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
		"serveFile", path.getFileName().toString()).build().toUri().toString())
		.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
		"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/") //This is used once someone would Post a request from the html page
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
	RedirectAttributes redirectAttributes) {
		if(files.size()==2){//No more than two files may be uploaded
			redirectAttributes.addFlashAttribute("message",
			"You Did not upload " + file.getOriginalFilename() + " because only two files allowed to upload!");
			return "redirect:/";
		}
		storageService.store(file);
		int numRows = 0;
		int numCols = 0;
		boolean valid = true;
		String reason = "";
		ArrayList<String[]> tempMatrix = new ArrayList<String[]>();
		//Creates a temporary arraylist and will be added to the actial matrix lists once the program checks the matrix is in the right format
		try {
			File matrix = new File("./uploadDir/" + file.getOriginalFilename());//loads the file
			Scanner reader = new Scanner(matrix);
			while (reader.hasNextLine() && valid) {//Goes through each line in the file
				numRows = numRows + 1;
				String currentRow = reader.nextLine();
				String[] rowMatrix = currentRow.split(" ");//Creates a list of all the columns in that row

				if(numRows==2){
					if(numCols!=rowMatrix.length){
						valid = false;
						reason = "All rows are not the same length";//Checks if all rows are the same size
					}
				}
				else{
					numCols = rowMatrix.length;
				}
				tempMatrix.add(rowMatrix);//Adds each row to the list
			}
			if(numRows!=numCols){
				valid = false;
				reason = "Not a square matrix";//Checks if the martix is sqaure
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			valid = false;
			reason = "File not Found";
		}
		if(valid==true){
			if(files.size()==0){
				matrix1 = tempMatrix;
			}
			else{
				matrix2 = tempMatrix;
			}
			redirectAttributes.addFlashAttribute("message",
			"You successfully uploaded " + file.getOriginalFilename() + "!");
			files.add(file.getOriginalFilename());
		}
		else{
			File matrix = new File("./uploadDir/" + file.getOriginalFilename());
			matrix.delete();//If the matrix is not formatted properly it deletes the file from the directory
			redirectAttributes.addFlashAttribute("message",
			"You did not upload " + file.getOriginalFilename() + " because " + reason + "!");
		}
		return "redirect:/";
	}

	@GetMapping("/add")
	public String addMatrix(RedirectAttributes redirectAttributes){
		if(files.size()==2){
			int matrix1Rows = matrix1.size();
			int matrix1Cols = matrix1.get(1).length;
			int matrix2Rows = matrix2.size();
			int matrix2Cols = matrix2.get(1).length;
			String rows = "";
			if(matrix1Rows!=matrix2Rows || matrix1Cols!=matrix2Cols){//martics must be same size to add them
				redirectAttributes.addFlashAttribute("message",
				"These matrixs cannot be added together as they are different sizes");
			}
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(matrix1Rows);
			for(int i =0; i<matrix1Rows; i++){
				String[] m1Row = matrix1.get(i);
				String[] m2Row = matrix2.get(i);
				rows = rows + "\n";
				ArrayList<String> rRow = new ArrayList<String>();
				for(int j = 0; j<matrix1Cols; j++){
					String value = Integer.toString(Integer.parseInt(m1Row[j])+Integer.parseInt(m2Row[j]));
					rRow.add(value);
					rows = rows + " " + value;
				}
			}
			redirectAttributes.addFlashAttribute("message",
			rows);
			// return GrpcServerServices.addMatrixs(matrix1, matrix2);
		}
		else{
			redirectAttributes.addFlashAttribute("message",
			"Please upload two files first");
		}
		return "redirect:/";
	}

	@GetMapping("/multi")//This method the maths is incorrect
	public String multiMatrix(RedirectAttributes redirectAttributes){
		if(files.size()==2){
			int matrix1Rows = matrix1.size();
			int matrix1Cols = matrix1.get(1).length;
			int matrix2Rows = matrix2.size();
			int matrix2Cols = matrix2.get(1).length;
			String rows = "";
			if(matrix1Rows!=matrix2Cols){//martics must be same size to add them
				redirectAttributes.addFlashAttribute("message",
				"These matrixs cannot be multiplied together as they are different sizes");
			}
			int MAX = matrix1.size();
		int blockSize = MAX/2;
		int C[][]= new int[MAX][MAX];
        for(int i=0;i<blockSize;i++){
            for(int j=0;j<blockSize;j++){
                for(int k=0;k<blockSize;k++){
                    C[i][j]+=(Integer.parseInt(matrix1.get(i)[k])*Integer.parseInt(matrix2.get(k)[j]));
                }
            }
        }
			for(int i=0;i<C.length;i++){
				rows = rows + "\n";
				for(int j=0;j<C.length;j++){
					rows = rows + " " + Integer.toString(C[i][j]);
				}
			}
			redirectAttributes.addFlashAttribute("message",
			rows);
			// return GrpcServerServices.addMatrixs(matrix1, matrix2);
		}
		else{
			redirectAttributes.addFlashAttribute("message",
			"Please upload two files first");
		}
		return "redirect:/";
	}
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
