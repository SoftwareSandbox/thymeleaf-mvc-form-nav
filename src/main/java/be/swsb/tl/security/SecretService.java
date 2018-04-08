package be.swsb.tl.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SecretService {

    private Map<SignatureAlgorithm, String> secrets = new HashMap<>();

    private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
            return TextCodec.BASE64.decode(secrets.get(SignatureAlgorithm.forName(header.getAlgorithm())));
        }
    };

    @PostConstruct
    public void setup() {
        refreshSecrets();
    }

    public SigningKeyResolver getSigningKeyResolver() {
        return signingKeyResolver;
    }

    public byte[] getHS256SecretBytes() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        return getSecretFor(signatureAlgorithm);
    }

    private byte[] getSecretFor(SignatureAlgorithm signatureAlgorithm) {
        return TextCodec.BASE64.decode(secrets.get(signatureAlgorithm));
    }

    private void refreshSecrets() {
        secrets = Stream.of(SignatureAlgorithm.HS256, SignatureAlgorithm.HS384, SignatureAlgorithm.HS512)
                .collect(Collectors.toMap(Function.identity(), signatureAlgToKey()));
    }

    private Function<SignatureAlgorithm, String> signatureAlgToKey() {
        return sa -> TextCodec.BASE64.encode(MacProvider.generateKey(sa).getEncoded());
    }
}
