package hiconic.rx.tika.wire.space;

import com.braintribe.mimetype.MimeTypeDetector;
import com.braintribe.utils.mime.TikaMimeTypeDetector;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import hiconic.rx.module.api.config.RxPlatformConfigurator;
import hiconic.rx.module.api.wire.RxModuleContract;
import hiconic.rx.module.api.wire.RxPlatformContract;

/**
 * Registers {@link TikaMimeTypeDetector} as the platform's {@link MimeTypeDetector}.
 */
@Managed
public class TikaRxModuleSpace implements RxModuleContract {

	@Import
	private RxPlatformContract platform;

	@Override
	public void configurePlatform(RxPlatformConfigurator configurator) {
		configurator.setMimeTypeDetector(new TikaMimeTypeDetector());
	}

}