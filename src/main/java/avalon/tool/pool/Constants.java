package avalon.tool.pool;

import avalon.tool.database.DatabaseOperator;
import avalon.tool.database.MySQLDatabaseOperator;
import avalon.tool.database.SQLiteDatabaseOperator;
import avalon.tool.system.Configs;
import avalon.util.servlet.AvalonServlet;
import avalon.util.servlet.CoolQServlet;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Created by Eldath on 2017/2/3 0003.
 *
 * @author Eldath
 */
public class Constants {
	private Constants() {
	}

	public static class Database {
		private static final String DATASOURCE =
				Configs.Companion.instance().getJSONObject("database").getString("datasource").toLowerCase();
		public static final DatabaseOperator CURRENT_DATABASE_OPERATOR =
				"mysql".equals(DATASOURCE) ? MySQLDatabaseOperator.getInstance() : SQLiteDatabaseOperator.getInstance();
	}

	public static class Address {
		public static final String SERVLET = Basic.CURRENT_SERVLET.apiAddress();
		public static final String SERVLET_SCRIPT_FILE = Basic.CURRENT_SERVLET.scriptFilePath();
		public static final String PERL_FILE_OF_WECHAT = Basic.CURRENT_PATH +
				File.separator + "bin" + File.separator + "Mojo-Weixin.pl";
		public static final String DATA_PATH = Basic.CURRENT_PATH + File.separator + "data";
	}

	public static class Version {
		public static final String AVALON = "1.1.0";
		private static final String SERVLET = Basic.CURRENT_SERVLET.version();
		private static Version instance = null;

		public static Version instance() {
			if (instance == null) instance = new Version();
			return instance;
		}

		public String servlet() {
			return SERVLET;
		}
	}

	public static class Basic {
		public static final AvalonServlet CURRENT_SERVLET =
				Configs.Companion.instance().getJSONObject("servlet").getString("servlet")
						.trim().equalsIgnoreCase("coolq") ? new CoolQServlet() : null;
		public static final boolean LOCAL_OUTPUT = (boolean) Configs.Companion.instance().get("local_output");
		public static final boolean DEBUG = (boolean) Configs.Companion.instance().get("debug");
		public static final long START_TIME = System.currentTimeMillis();
		public static final long DEBUG_MESSAGE_UID = 10000;
		public static final int PID = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
		public static final int MAX_ECHO_LENGTH = 160;
		public static final String CURRENT_PATH;

		static {
			String path1;
			try {
				path1 = new File("").getCanonicalPath();
			} catch (IOException e) {
				System.err.println("Fatal error: " + e.toString());
				path1 = "";
				System.exit(-1);
			}
			CURRENT_PATH = path1;
		}
	}

	public static class Setting {
		public static final boolean Block_Words_Punishment_Mode_Enabled =
				(boolean) Configs.Companion.instance().get("block_words_punishment_mode_enabled");

		public static final boolean RSS_Enabled = Configs.INSTANCE.isPluginEnable("RSS");
	}
}
