package com.njnu.kai.feisms;

public class ChooseContactsForDisplay {
	public long mContactsId;
	public String mName;
	public String mPhone;
	public HanziToPinyin.Pinyin mPinyin;

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder(32);
		build.append(mName);
		while (build.length() < 3) {
			build.append('\u3000'); // 全角空格
		}
		build.append(": ");
		build.append(mPhone);
		return build.toString();
	}

	private boolean isRightAccordWith(char[] t9KeyItem, char[] digitalText, int idxDigital) {
		boolean accord = true;
		int idxT9Key = 1;
		++idxDigital;
		for (; idxT9Key < t9KeyItem.length && idxDigital < digitalText.length; ++idxT9Key, ++idxDigital) {
			if (t9KeyItem[idxT9Key] != digitalText[idxDigital]) {
				accord = false;
				break;
			}
		}
		return accord;
	}
	
	private boolean isSubAccordWith(char[] digitalText, char[][] t9Key, int t9KeyFromIndex) {
		boolean accord = false;
		int digitalLen = digitalText.length;
		int idxDigital = 0;
		int idxT9Key = t9KeyFromIndex;
		for (; idxT9Key < t9Key.length; ++idxT9Key) {
			char[] t9KeyItem = t9Key[idxT9Key];
			if (t9KeyItem == null) {
				continue;
			}
			if (t9KeyItem[0] != digitalText[idxDigital]) {
				break;
			} else {
				if (idxDigital == digitalLen - 1) {
					accord = true;
					break;
				}
				if (isRightAccordWith(t9KeyItem, digitalText, idxDigital)) {
					idxDigital += t9KeyItem.length;
					if (idxDigital >= digitalLen) {
						accord = true;
						break;
					}
				} else {
					++idxDigital;
				}
			}
		}

		return accord;
	}

	public boolean isAccordWith(String digitalText) {
		char[] arrDigitalText = digitalText.toCharArray();
		boolean accord = false;
		char[][] t9Key = mPinyin.getT9Key();
		for (int idx = 0; idx < t9Key.length; ++idx) {
			char[] t9KeyItem = t9Key[idx];
			if (t9KeyItem != null) {
				accord = isSubAccordWith(arrDigitalText, t9Key, idx);
				if (accord) {
					break;
				}
			}
		}

		return accord;
	}
}