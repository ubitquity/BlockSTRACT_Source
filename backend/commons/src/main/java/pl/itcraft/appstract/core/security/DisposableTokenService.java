package pl.itcraft.appstract.core.security;


import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DisposableTokenService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DisposableTokenService.class);
	
	@Value("${app.disposable_token.ttl_minutes}")
	private int validityMinutesLimit;
	
	@Value("${app.disposable_token.secret}")
	private String secret;
	
	private final int SALT_LENGTH = 8;
	private final Pattern SIGNED_TOKEN_PATTERN;
	
	private final String TOKEN_FORMAT_DELIMITER = "\t";
	
	public DisposableTokenService() {
		SIGNED_TOKEN_PATTERN = Pattern.compile(
			"[a-zA-Z0-9]+" + TOKEN_FORMAT_DELIMITER + // salt
			"[1-9][0-9]*" + TOKEN_FORMAT_DELIMITER + // timestamp
			"[a-zA-Z0-9_-]+" + TOKEN_FORMAT_DELIMITER + // key (URL-safe Base64 encoded)
			"[a-zA-Z0-9]+" // signature
		);
	}
	
	public String createToken(String keyToSecure) {
		if (!StringUtils.hasLength(keyToSecure)) {
			throw new RuntimeException("Key can`t be empty");
		}
		String salt = RandomStringUtils.random(SALT_LENGTH, true, true);
		String phraseToSign = formatToken(
			salt,
			String.valueOf(new Date().getTime()),
			Base64.encodeBase64URLSafeString(keyToSecure.getBytes())
		);
		String signedToken = phraseToSign + TOKEN_FORMAT_DELIMITER + sign(phraseToSign);
		return Base64.encodeBase64URLSafeString( signedToken.getBytes() );
	}
	
	public String decodeToken(String signedTokenBase64) {
		if (StringUtils.hasLength(signedTokenBase64)) {
			String signedToken = new String( Base64.decodeBase64(signedTokenBase64) );
			if (SIGNED_TOKEN_PATTERN.matcher(signedToken).matches()) {
				String[] parts = signedToken.split(TOKEN_FORMAT_DELIMITER);
				if (parts.length == 4) {
					String phraseToSign = formatToken(parts[0], parts[1], parts[2]);
					String signature = parts[3];
					if (signature.equals(sign(phraseToSign))) {
						Date ts = new Date( Long.parseLong(parts[1]) );
						Date range = DateUtils.addMinutes(new Date(), (-1) * validityMinutesLimit);
						if (range.before(ts)) {
							return new String( Base64.decodeBase64(parts[2]) );
						} else {
							LOG.info("Token expired: {}", signedTokenBase64);
							return null;
						}
					} else {
						LOG.info("Bad token signature: {}", signedTokenBase64);
						return null;
					}
				}
			}
		}
		LOG.info("Token corrupted: {}", signedTokenBase64);
		return null;
	}
	
	private String sign(String phraseToSign) {
		// SHA-1 wystarczy
		// W razie potrzeby mozna uzyc sha256Hex() lub sha512Hex()
		return DigestUtils.shaHex(phraseToSign + secret);
	}
	
	private String formatToken(String salt, String timestamp, String key) {
		return salt + TOKEN_FORMAT_DELIMITER + timestamp + TOKEN_FORMAT_DELIMITER + key;
	}

}