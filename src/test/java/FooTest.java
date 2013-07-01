import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.facade.APIWrapperFactory;
import org.codeforamerica.open311.facade.City;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.junit.Test;

public class FooTest {

	@Test
	public void somethingTest() throws APIWrapperException {
		try {
			APIWrapper wrapper = new APIWrapperFactory(City.BOSTON).build();
			System.out.println("Built!");
			List<Service> services = wrapper.getServiceList();
			for (Service service : services) {
				System.out.println(service.getServiceName());
			}
		} catch (APIWrapperException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getError());
			System.out.println(e.getGeoReportErrors());
			throw new APIWrapperException("", null, null);
		}
	}
}
