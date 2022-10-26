import java.sql.*;
import java.util.Scanner;


public class OttSurvey {
   int check = 0;
   Scanner sc = new Scanner(System.in);

   public void runSurvey(Connection connection, Statement statement, PreparedStatement preparedStatement) {
      
      String query = "SELECT * FROM questions ORDER BY QUESTIONS_UID"; // 질문 리스트 조회
      // 입력값 name을 받아서 사용자 번호 조회, pre~사용 쿼리문
      String queryUid = "SELECT PARTICIPANTS_UID, NAME FROM PARTICIPANTS";

      System.out.println("이름을 입력하세요.");
      String name = sc.nextLine();

      if (name.equals("")) {
         System.out.println("잘못된 입력입니다.");

      } else {

         try {
            // 질문 쿼리 조회 실행
            ResultSet rs = statement.executeQuery(query);
            // 사용자 번호 조회 쿼리 실행
            int lastPid = 0;
            preparedStatement = connection.prepareStatement(queryUid);
            ResultSet rsl = preparedStatement.executeQuery();
            
            while(rsl.next()){
               lastPid = Integer.parseInt(rsl.getString("PARTICIPANTS_UID"));
            }
            lastPid++; //--> sql insert 할 때 넣어야 할 PID
            // rsl.next();
            // String participants_uid = rsl.getString("PARTICIPANTS_UID");
            System.out.println(lastPid + "+++++++++++++++++++++++++++");


            System.out.println("-모든 질문의 답은 한가지만 선택 가능합니다.-");
            System.out.println("-------------------------------------------");

            while (rs.next()) {
               String questions_uid = rs.getString("QUESTIONS_UID");
               System.out.println("Q" + questions_uid + ".");
               System.out.println(rs.getString("CONTENTS"));
               answerList(connection ,preparedStatement ,statement, name, Integer.toString(lastPid), questions_uid); // 답변리스트 조회로
            }
               
               // connection.close();
               // preparedStatement.close();
               // statement.close();
               
         } catch (SQLException e) {
            e.printStackTrace();
            
         }

      }
   }

   //
   public void answerList(Connection connection, PreparedStatement preparedStatement, Statement statement, String name, String lastPid, String questions_uid) {
      String query = "SELECT * FROM ANSWERS ORDER BY ANSWER_UID";

      try {
         Statement st = connection.createStatement();
         ResultSet rs = st.executeQuery(query);
         String answer_uid="";

         while (rs.next()) {
            
            answer_uid = rs.getString("ANSWER_UID");
            System.out.print("A" + answer_uid + ".");
            System.out.print(rs.getString("ANSWER")+"    ");
         }
            System.out.println("      ");
            System.out.println("답:>>>");
            String answer_id = sc.nextLine();  
            saveSurvey(connection, preparedStatement, statement, name, lastPid, questions_uid, answer_id);
            

         // connection.close();
         // preparedStatement.close();
         // statement.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   //오류 1. java.sql.SQLException: Statement.executeQuery() cannot issue statements that do not produce result sets. 
   // : Statement.executeQuery()는 ResultSet을 반환하는 메소드... 근데 고쳐봐도 해결이 안됨; insert문에 executeQuery 아니고 execute를 사용하면 다른 에러가 뜸.(Cannot add or update a child row: a foreign key constraint fails)

   //오류 2. java.sql.SQLException: Operation not allowed after ResultSet closed 
   //statement의 executeQuery는 반복 수행할 수 없다->한 번 수행 후에는 close 후에 반복될 때마다 statement를 새로 생성해서 사용해보기

   // result 결과값 저장하는 쿼리
   public void saveSurvey(Connection connection, PreparedStatement preparedStatement, Statement statement, 
   String name, String lastPid, String questions_uid, String answer_id) {
      try {
         
         String q= "INSERT INTO participants (`PARTICIPANTS_UID`, `NAME`) VALUES (?,?)";
         String query = "INSERT INTO result (PARTICIPANTS_UID, QUESTIONS_UID, ANSWER_UID) VALUES (?, ?, ?)";
         
         if(check == 0){
            preparedStatement = connection.prepareStatement(q);
         
            preparedStatement.setString(1, lastPid);
            preparedStatement.setString(2, name);
            preparedStatement.execute();
            check++;
         }
         
         preparedStatement = connection.prepareStatement(query);
         
         preparedStatement.setString(1, lastPid);
         preparedStatement.setString(2, questions_uid);
         preparedStatement.setString(3, answer_id);
         preparedStatement.execute();
         // connection.close();
         // preparedStatement.close();
         // statement.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}        
         
      
    /*  -- 회원 가입
      INSERT INTO `semi_project`.`participants` (`PARTICIPANTS_UID`, `NAME`) VALUES ('1', '박병주');
      -- 답안 출력
      SELECT answer FROM (questions
      INNER JOIN questions_answers ON questions_answers.QUESTIONS_UID = questions.QUESTIONS_UID)
      INNER JOIN answers ON questions_answers.ANSWER_UID = answers.ANSWER_UID
      WHERE questions.QUESTIONS_UID = '1';
      -- 사용자 답 입력
      INSERT INTO result (PARTICIPANTS_UID, ANSWER_UID, QUESTIONS_UID) VALUES ('1', '1', '1');*/