package de.tmtomcat.em.webclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMessages_de;

import de.tmtomcat.em.ServerManager;

/**
 * Servlet implementation class WebClient
 */
@WebServlet(description = "Client for useage in Browsers", urlPatterns = { "/WebClient" })
public class WebClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServerManager manager;

	@Override
	public void init() throws ServletException {
		super.init();
		manager = new ServerManager(getServletContext()
				.getRealPath("/UserData"));
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// System.out.println(manager.doGet());
		HttpSession session = request.getSession();

		session.setAttribute(
				"kunde",
				getKunde(request.getParameter("username"),
						request.getParameter("password")));
		session.setAttribute("btnNames", manager.doGet());
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("client.jsp");
		dispatcher.forward(request, response);
	}

	public synchronized Kunde getKunde(String username, String password) {
		Kunde kunde = new Kunde();
		if (username == null || username.length() < 1 || password == null
				|| password.length() < 1) {
			return kunde;
		}
		try {
			kunde.setId(000L);
			kunde.setName(username);
			kunde.setPassword(password);
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return kunde;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			switch (request.getParameter("command")) {
			case "uploadData":
				uploadData(request, response);
				break;
			case "loadDataName":
				sendDataNameInfo(request, response);
				break;
			default:
				response.getWriter().println(
						"Unknown command: " + request.getParameter("command"));
				break;
			}
		} catch (Exception e) {
			request.getSession().setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("client.jsp").forward(request,
					response);
		}
	}

	private void uploadData(HttpServletRequest request,
			HttpServletResponse response) throws JSONException, IOException,
			ServletException {
		ArrayList<String> nameArray1 = (ArrayList<String>) request.getSession()
				.getAttribute("dataNames");
		String[] nameArray = nameArray1.toArray(new String[] {});
		Integer[] dataArray;
		try {
			dataArray = new Integer[nameArray.length];
			for (int i = 0; i < nameArray.length; i++) {
				dataArray[i] = Integer.parseInt(request
						.getParameter(nameArray[i]));
				if (dataArray[i] < 0)
					throw new NumberFormatException();
				
			}
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(ErrorMessage.get());
		}

		manager.uploadData((String) request.getSession().getAttribute("Name"),
				nameArray, dataArray);

		// session.setAttribute("btnNames", manager.doGet());
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("client.jsp");
		dispatcher.forward(request, response);
	}

	private void sendDataNameInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String> dataNames = new ArrayList<String>();
		for (Object dataName : manager.getDataNameInfo(request
				.getParameter("dataName"))) {
			dataNames.add((String) dataName);
		}
		HttpSession session = request.getSession();
		session.removeAttribute("errorMessage");
		session.setAttribute("dataNames", dataNames);
		// session.setAttribute("Name", request.getParameter("dataName"));
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("client.jsp");// "input.jsp"
		dispatcher.forward(request, response);
	}

}
