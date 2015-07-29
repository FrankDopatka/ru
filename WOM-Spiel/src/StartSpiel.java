import java.io.IOException;

import frontend.Frontend;

public class StartSpiel {
	public static void main(String[] args) throws IOException{
	//				final String zumServer="http://134.103.176.80:8000";
					final String zumServer="http://192.168.178.33:8000";
		final int spielerAnzahl=2;
		
		for(int i=1;i<=spielerAnzahl;i++){
			final int idSpieler=i;
			new Thread(){
				@Override
				public void run(){
					new Frontend(zumServer,idSpieler);
				}
			}.start();
		}
	}
}
