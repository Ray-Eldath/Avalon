package test;

import avalon.util.backend.CoolQBackend;

/**
 * Created by Eldath Ray on 2017/6/10 0010.
 *
 * @author Eldath Ray
 */
public class CoolQServletTest {
	public static void main(String[] args) {
		System.out.println(CoolQBackend.INSTANCE().available());
	}
}
