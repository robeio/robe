package io.robe.auth;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public final class Randomizer {
	private static Logger LOGGER = Logger.getLogger(Randomizer.class.getName());
	private static final Randomizer INSTANCE = new Randomizer();


	public static final Randomizer getInstance() throws Exception {
		return INSTANCE;
	}

	SecretKey SECRET;


	private Randomizer() throws RuntimeException {
		try {
			LOGGER.info("Constructing Randomizer...");
			recreateSecureRandomInstance();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO synchronized must be used  at reseeding not at mehod
	 *
	 * @throws RuntimeException
	 */
	private synchronized void reseedRandomAsNeeded() throws RuntimeException {
		LOGGER.info("SecureRandom re-seeding:" + Thread.currentThread().getId());

		try {
			recreateSecureRandomInstance();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}


	private void recreateSecureRandomInstance() throws NoSuchAlgorithmException {
		SECRET = KeyGenerator.getInstance("AES").generateKey();
	}


	public static final SecretKey getKey() throws Exception {
		return getInstance().SECRET;
	}


}