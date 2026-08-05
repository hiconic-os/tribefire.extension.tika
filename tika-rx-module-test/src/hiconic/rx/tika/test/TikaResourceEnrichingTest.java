// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package hiconic.rx.tika.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resource.specification.RasterImageSpecification;

import hiconic.rx.access.module.api.AccessContract;
import hiconic.rx.test.common.AbstractRxTest;

public class TikaResourceEnrichingTest extends AbstractRxTest {

	@Test
	public void detectsContentAndThenDerivesImageSpecification() throws IOException {
		byte[] png = png(3, 2);
		PersistenceGmSession session = resolveExportContract(AccessContract.class).systemSessionFactory().newSession("main-access");

		Resource resource = session.resources().create() //
				.name("deliberately-misleading.txt") //
				.store(new ByteArrayInputStream(png));

		assertThat(resource.getMimeType()).isEqualTo("image/png");
		assertThat(resource.getFileSize()).isEqualTo((long) png.length);
		assertThat(resource.getMd5()).isNotBlank();
		assertThat(resource.getSpecification()).isInstanceOf(RasterImageSpecification.class);

		RasterImageSpecification specification = (RasterImageSpecification) resource.getSpecification();
		assertThat(specification.getPixelWidth()).isEqualTo(3);
		assertThat(specification.getPixelHeight()).isEqualTo(2);
	}

	private static byte[] png(int width, int height) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (!ImageIO.write(image, "png", out))
			throw new IllegalStateException("No PNG ImageIO writer available");
		return out.toByteArray();
	}
}
