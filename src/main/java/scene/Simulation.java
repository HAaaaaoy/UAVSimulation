package scene;

import UAVs.UAV;
import java.util.Random;


public class Simulation {
	public WirelessChannel wifi;
	public UAV uav;
	private Random random;
	public Simulation(WirelessChannel wifi) {
		this.wifi = wifi;
	}

	public void ListeningChannel() throws InterruptedException {
		if (wifi.wifistatus == WirelessChannelStatus.Busy) {
			//退避
			uav.sleep(uav.getBackoff());
		} else if(wifi.wifistatus == WirelessChannelStatus.Idle) {
			uav.generatePacket(random.nextInt(64));

			wifi.wifistatus = WirelessChannelStatus.Busy;
			//传输时间，加锁睡眠
			uav.sleep(wifi.delay(random.nextInt(1000)));
			wifi.wifistatus = WirelessChannelStatus.Idle;
		} else {
			uav.sleep(uav.getBackoff());
		}
	}
}