// package com.example.uploadingfiles;
//

// This file is full of a version of the server side that i could never figure out
// I couldn't figure out how to get this file to work
//

// // import com.example.grpc.server.grpcserver.MatrixRequest;
// // import com.example.grpc.server.grpcserver.MatrixReply;
// // import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
// import io.grpc.ManagedChannel;
// import io.grpc.ManagedChannelBuilder;
// import net.devh.boot.grpc.client.inject.GrpcClient;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.stereotype.Service;
// import java.util.*;
// @Service
// public class GRPCServerServices {
//   public String addMatrixs(	ArrayList<String[]> matrix1, ArrayList<String[]> matrix2){
//     int matrix1Rows = matrix1.size();
//     int matrix1Cols = matrix1.get(1).length;
//     int matrix2Rows = matrix2.size();
//     int matrix2Cols = matrix2.get(1).length;
//     if(matrix1Rows!=matrix2Rows || matrix1Cols!=matrix2Cols){
//       return "These matrixs cannot be added together as they are different sizes";
//     }
//     // ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8080)
//     // .usePlaintext()
//     // .build();
//     // MatrixServiceGrpc.MatrixServiceBlockingStub stub
//     // = MatrixServiceGrpc.newBlockingStub(channel);
//     // MatrixReply A=stub.addBlock(MatrixRequest.newBuilder());
//     // int lenofBinary = (matrix1Rows*matrix1Cols).length;
//     // for(int i = 0; i<matrix1Rows*matrix1Cols; i++){
//     //   String binary = Integer.toBinaryString(i);
//     //   int addzeros = lenofBinary-binary.length;
//     //   for(int j = 0; j<addzeros; j++){
//     //     binary = "0"+binary;
//     //   }
//     //   int num = matrix1[Math.floor(i/matrix1Cols)][i%matrix1Cols];
//     //   A.setA"binary"(num);
//     // }
//     // lenofBinary = (matrix2Rows*matrix2Cols).length;
//     // for(int i = 0; i<matrix2Rows*matrix2Cols; i++){
//     //   String binary = Integer.toBinaryString(i);
//     //   int addzeros = lenofBinary-binary.length;
//     //   for(int j = 0; j<addzeros; j++){
//     //     binary = "0"+binary;
//     //   }
//     //   int num = matrix1[Math.floor(i/matrix1Cols)][i%matrix1Cols];
//     //   String index = "setB"+binary;
//     //   A.setB"binary"(num);
//     // }
//     // // .setA00(1)
//     // // .setA01(2)
//     // // .setA10(5)
//     // // .setA11(6)
//     // // .setB00(1)
//     // // .setB01(2)
//     // // .setB10(5)
//     // // .setB11(6)
//     // A.build();
//     // String resp= A.getC00()+" "+A.getC01()+"<br>"+A.getC10()+" "+A.getC11()+"\n";
//     return resp;
//   }
// }
