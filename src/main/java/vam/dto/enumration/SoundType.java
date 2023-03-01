package vam.dto.enumration;

import java.util.Arrays;
import java.util.List;

public enum SoundType {
	MOAN(Arrays.asList("MOAN", "呻吟聲")), FAKE_REJECT(Arrays.asList("不要", "BGO-CM-")),
	PORN_WORDS_ACT(Arrays.asList("DG-ACT-", "DG-AT-")), PORN_WORDS_CUM(Arrays.asList("DG-CM-", "DG-CUM-")),
	CUM(Arrays.asList("-O-", "CUM")), PORN_WORDS(Arrays.asList("淫語")), BLOW(Arrays.asList("BLOW")),
	KISS(Arrays.asList("KISS")), BREATH(Arrays.asList("BREATH")), PHONERING(Arrays.asList("PHONERING")),
	NORMAL(Arrays.asList("BUSINESS")), PAPACUM(Arrays.asList("PAPACUM")), SPANK(Arrays.asList("SPANK")),
	PORN_WORDS_SPANK(Arrays.asList("SPANK")), WET(Arrays.asList("WET")),;

	private List<String> kindList;

	SoundType(List<String> kindList) {
		this.kindList = kindList;
	}
}
