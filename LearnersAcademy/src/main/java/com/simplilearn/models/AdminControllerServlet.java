  package com.simplilearn.models;
  import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

  import javax.annotation.Resource;
  import javax.servlet.RequestDispatcher;
  import javax.servlet.ServletException;
  import javax.servlet.annotation.WebServlet;
  import javax.servlet.http.Cookie;
  import javax.servlet.http.HttpServlet;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import javax.sql.DataSource;

  import com.simplilearn.models.Students;
  import com.simplilearn.models.Subject;
  import com.simplilearn.models.Teacher;
  import com.simplilearn.models.Class;
 

  @WebServlet("/AdminControllerServlet")
  
public class AdminControllerServlet extends HttpServlet{

	  private static final long serialVersionUID = 1L;

		private DbRetrieve dbRetrieve;

		@Resource(name = "jdbc_database")
		private DataSource datasource;

		private PrintWriter out;

		private boolean flag;

		@Override
		public void init() throws ServletException {

			super.init();

					try {
				dbRetrieve = new DbRetrieve(datasource);

			} catch (Exception e) {
				throw new ServletException(e);
			}

		}

		
		public AdminControllerServlet() {
			super();
			
		}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

			doGet(req, resp);
			  Cookie[] cookies = req.getCookies();
              out = resp.getWriter();              
              for(int i=0;i<cookies.length;i++){
                  if(cookies[i].getName().equals("session started")){
                      flag=true;
                      out.println("Your session started on "+cookies[i].getValue());
                  }
              }
		}
		
		
		 
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			
			// TODO Auto-generated method stub
			try {

			
				String command = request.getParameter("command");

				if (command == null) {
					command = "CLASSES";
				}
				
			
				 if(!flag){
	                    String time = new java.util.Date().toString();
	                    response.addCookie(new Cookie("session started",time));
	                    out.println("just started");
				if (!getCookies(request, response) && (!command.equals("LOGIN"))) {

					response.sendRedirect("/Administrative-Portal/login.jsp");
				}

				else {

					
					switch (command) {

					case "STUDENTS":
						studentsList(request, response);
						break;

					case "TEACHERS":
						teachersList(request, response);
						break;

					case "SUBJECTS":
						subjectList(request, response);
						break;

					case "CLASSES":
						classestList(request, response);
						break;

					case "ST_LIST":
						classStudentsList(request, response);
						break;

					case "LOGIN":
						login(request, response);
						break;

					default:
						classestList(request, response);

					}
				}
				 }
			}
			 catch (Exception e) {
				throw new ServletException(e);
			}
			
		}

		private void studentsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			List<Students> students = dbRetrieve.getStudents();

			
			request.setAttribute("STUDENT_LIST", students);

			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
			dispatcher.forward(request, response);

		}

		private void teachersList(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			List<Teacher> teachers = dbRetrieve.getTeachers();

			
			request.setAttribute("TEACHERS_LIST", teachers);

			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/teachers-list.jsp");
			dispatcher.forward(request, response);

		}

		private void subjectList(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			List<Subject> subjects = dbRetrieve.getSubjects();

			
			request.setAttribute("SUBJECTS_LIST", subjects);

			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/subjects-list.jsp");
			dispatcher.forward(request, response);

		}

		private void classestList(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			List<Class> classes = dbRetrieve.getClasses();

			
			request.setAttribute("CLASSES_LIST", classes);

			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/classes-list.jsp");
			dispatcher.forward(request, response);

		}

		private void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (username.toLowerCase().equals("admin") && password.toLowerCase().equals("admin")) {

				Cookie cookie = new Cookie(username, password);

				
				cookie.setMaxAge(86400); 

				response.addCookie(cookie);
				classestList(request, response);
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
				dispatcher.forward(request, response);
			}

		}

		private void classStudentsList(HttpServletRequest request, HttpServletResponse response) throws Exception {

			int classId = Integer.parseInt(request.getParameter("classId"));
			String section = request.getParameter("section");
			String subject = request.getParameter("subject");

			
			List<Students> students = dbRetrieve.loadClassStudents(classId);

			
			request.setAttribute("STUDENTS_LIST", students);
			request.setAttribute("SECTION", section);
			request.setAttribute("SUBJECT", subject);

			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/class-students.jsp");
			dispatcher.forward(request, response);

		}

		private boolean getCookies(HttpServletRequest request, HttpServletResponse response) throws Exception {

			boolean check = false;
			Cookie[] cookies = request.getCookies();
			
			for (Cookie cookie : cookies) {
				 
				if (cookie.getName().equals("admin") && cookie.getValue().equals("admin")) {
					check = true;
					break;
				} 

			}
			
			
			return check;

		}
}
