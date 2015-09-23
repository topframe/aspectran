/*
 * Copyright 2008-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.core.util.wildcard;

import com.aspectran.core.util.StringUtils;

/**
 * The Class WildcardMatcher.
 */
public class WildcardMatcher {

	private WildcardPattern pattern;
	
	private CharSequence input;
	
	private int[] separatorFlags;
	
	private int separatorCount;
	
	private int separatorIndex;
	
	public WildcardMatcher(WildcardPattern pattern) {
		this.pattern = pattern;
	}
	
	public boolean matches(CharSequence input) {
		separatorCount = 0;
		separatorIndex = 0;

		if(input == null) {
			this.input = null;
			separatorFlags = null;
			return false;
		}
		
		this.input = input;
		separatorFlags = new int[input.length()];
		
		boolean result = matches(pattern, input, separatorFlags);
		
		if(result) {
			for(int i = separatorFlags.length - 1; i >= 0; i--) {
				if(separatorFlags[i] > 0) {
					separatorCount = separatorFlags[i];
					break;
				}
			}
		}
		
		return result;
	}
	
	public WildcardMatcher first() {
		separatorIndex = 0;
		return this;
	}

	public WildcardMatcher last() {
		separatorIndex = separatorCount;
		return this;
	}
	
	public boolean hasNext() {
		return separatorIndex <= separatorCount;
	}

	public boolean hasPrev() {
		return separatorIndex >= 0;
	}
	
	public String next() {
		if(separatorIndex > separatorCount)
			return null;

		return find(separatorIndex++);
	}
	
	public String prev() {
		if(separatorIndex < 0)
			return null;
		
		return find(separatorIndex--);
	}

	public String find() {
		return find(separatorIndex);
	}
	
	public String find(int group) {
		if(separatorCount == 0) {
			if(input == null)
				return null;
			
			return input.toString();
		}
		
		if(group < 0 || group > separatorCount)
			throw new IndexOutOfBoundsException();
		
		int start = 0;
		int offset = -1;
		
		if(group == 0) {
			for(int i = 0; i < separatorFlags.length; i++) {
				if(separatorFlags[i] == 1) {
					offset = i;
					break;
				}
			}

			if(offset == -1)
				offset = separatorFlags.length;
		} else {
			for(int i = 0; i < separatorFlags.length; i++) {
				if(separatorFlags[i] == group) {
					start = i + 1;
				} else if(start > 0 && separatorFlags[i] == group + 1) {
					offset = i;
					break;
				}
			}
			
			if(start > 0 && offset == -1) {
				offset = separatorFlags.length;
			}

		}
		
		if(offset == -1)
			return null;
		else if(offset == 0)
			return StringUtils.EMPTY;
		else
			return input.subSequence(start, offset).toString();
	}
	
	public int getSeparatorCount() {
		return separatorCount;
	}
	
	public WildcardPattern getWildcardPattern() {
		return pattern;
	}
	
	public static boolean matches(WildcardPattern pattern, CharSequence input) {
		return matches(pattern, input, null);
	}
	
	/**
	 * @param pattern
	 * @param input
	 * @param separatorFlags
	 * @return
	 */
	private static boolean matches(WildcardPattern pattern, CharSequence input, int[] separatorFlags) {
		char[] tokens = pattern.getTokens();
		int[] types = pattern.getTypes();
		char[] separators = pattern.getSeparator();
		
		int tokensLength = tokens.length;
		int inputLength = input.length();

		int sepaLength = separators == null ? -1 : separators.length;
		int sepaCount = 0;

		int tokenIndex = 0;
		int inputIndex = 0;
		
		while(tokenIndex < tokensLength && inputIndex < inputLength) {
			if(types[tokenIndex] == WildcardPattern.LITERAL_TYPE) {
				if(tokens[tokenIndex++] != input.charAt(inputIndex++))
					return false;
			} else if(types[tokenIndex] == WildcardPattern.STAR_TYPE) {
				int t1 = tokenIndex + 1;
				int t2 = t1;
				for(; t2 < tokensLength; t2++) {
					if(types[t2] == WildcardPattern.EOT_TYPE || types[t2] != WildcardPattern.LITERAL_TYPE)
						break;
				}
				
				if(t1 == t2) {
					inputIndex++;
					tokenIndex++;
				} else {
					int t = t1;
					do {
						if(tokens[t] != input.charAt(inputIndex++))
							t = t1;
						else
							t++;
					} while(t < t2 && inputIndex < inputLength);
					if(t < t2)
						return false;
					tokenIndex = t2;;
				}
			} else if(types[tokenIndex] == WildcardPattern.STAR_STAR_TYPE) {
				if(sepaLength > 0) {
					//"com.**.*scope.**.*Xml*"
					//"com.**.**.?scope*.**.*Xml*"
					//"com//j/j/*scope.**.*Xml*"
					int t1 = -1;
					int t2 = -1;
					for(int n = tokenIndex + 1; n < tokensLength; n++) {
						if(t1 == -1) {
							if(types[n] == WildcardPattern.LITERAL_TYPE) {
								t1 = n;
							}
						} else {
							if(types[n] == WildcardPattern.SEPARATOR_TYPE) {
								t2 = n + sepaLength - 1;
								break;
							} else if(types[n] != WildcardPattern.LITERAL_TYPE) {
								t2 = n - 1;
								break;
							}
						}
					}
					if(t1 > -1 && t2 > -1) {
						if(t1 > tokenIndex + 3 && types[t1 - 1] == WildcardPattern.SEPARATOR_TYPE) {
							t1 -= sepaLength;
						}
						int c1 = inputIndex;
						int t = t1;
						while(t <= t2 && c1 < inputLength) {
							if(input.charAt(c1) != tokens[t]) {
								t = t1;
							} else {
								t++;
							}
							c1++;
						}
						if(t <= t2) {
							return false;
						}
						c1--;
						int caIndex2 = inputIndex;
						if(types[t1] == WildcardPattern.SEPARATOR_TYPE) {
							inputIndex = c1 + 1;
							tokenIndex = t2 + 1;
						} else {
							if(sepaLength == 1) {
								if(types[t2] == WildcardPattern.SEPARATOR_TYPE)
									c1--;
								for(; c1 >= inputIndex; c1--) {
									if(input.charAt(c1) == separators[0]) {
										inputIndex = c1;
										break;
									}
								}
							} else {
								while(types[t2] != WildcardPattern.SEPARATOR_TYPE && t2 > t1) {
									t2--;
									c1--;
								}
								int s = sepaLength - 1;
								while(s >= 0 && c1 < inputLength) {
									if(input.charAt(c1) == separators[s])
										s--;
									else
										s = sepaLength - 1;
									c1--;
								}
								if(s == -1) {
									inputIndex = c1 + 1;
								}
							}
							tokenIndex++;
							if(inputIndex == caIndex2 && types[tokenIndex] == WildcardPattern.SEPARATOR_TYPE)
								tokenIndex++;
						}

						if(separatorFlags != null && caIndex2 < inputIndex) {
							if(sepaLength == 1) {
								for(int k = inputIndex - 1; k >= caIndex2; k--) {
									if(input.charAt(k) == separators[0])
										separatorFlags[k] = ++sepaCount;
								}
							} else {
								int k = inputIndex - 1;
								while(k >= caIndex2) {
									int s = sepaLength - 1;
									while(s >= 0) {
										if(input.charAt(k) == separators[s])
											s--;
										else
											s = sepaLength - 1;
										k--;
									}
									if(s == -1) {
										++sepaCount;
										for(s = 0; s < sepaLength; s++) {
											separatorFlags[k + s] = sepaCount;
										}
									}
								}
							}
						}
					} else {
						tokenIndex++;
					}
				} else {
					inputIndex = inputLength; //complete
					tokenIndex++;
				}
			} else if(types[tokenIndex] == WildcardPattern.QUESTION_TYPE) {
				if(tokenIndex > tokensLength - 1 ||
						types[tokenIndex + 1] != WildcardPattern.LITERAL_TYPE ||
						tokens[tokenIndex + 1] != input.charAt(inputIndex)) {
					if(sepaLength > 0) {
						if(sepaLength == 1) {
							if(input.charAt(inputIndex) != separators[0])
								inputIndex++;
						} else {
							int s = sepaLength - 1;
							if(inputIndex + s < inputLength) {
								for(; s >= 0; s--) {
									if(input.charAt(inputIndex + s) != separators[s])
										break;
								}
							}
							if(s != -1)
								inputIndex++;
						}
					} else {
						inputIndex++;
					}
				}
				tokenIndex++;
			} else if(types[tokenIndex] == WildcardPattern.PLUS_TYPE) {
				if(sepaLength > 0) {
					if(sepaLength == 1) {
						if(input.charAt(inputIndex) == separators[0])
							return false;
					} else {
						if(inputIndex + sepaLength - 1 < inputLength) {
							int s = sepaLength - 1;
							for(; s >= 0; s--) {
								if(input.charAt(inputIndex + s) != separators[s])
									break;
							}
							if(s == -1)
								return false;
						}
					}
				}
				inputIndex++;
				tokenIndex++;
			} else if(types[tokenIndex] == WildcardPattern.SEPARATOR_TYPE) {
				if(sepaLength == 1) {
					if(tokens[tokenIndex++] != input.charAt(inputIndex++))
						return false;
					if(separatorFlags != null)
						separatorFlags[inputIndex - 1] = ++sepaCount;
				} else {
					if(inputIndex + sepaLength > inputLength)
						return false;
					for(int s = 0; s < sepaLength; s++) {
						if(tokens[tokenIndex++] != input.charAt(inputIndex++))
							return false;
					}
					if(separatorFlags != null) {
						++sepaCount;
						for(int s = sepaLength - 1; s >= 0; s--) {
							separatorFlags[inputIndex - s - 1] = sepaCount;
						}
					}
				}
			} else if(types[tokenIndex] == WildcardPattern.EOT_TYPE) {
				break;
			} else {
				tokenIndex++;
			}
		}
		
		if(tokenIndex < tokensLength) {
			for(int i = tokenIndex; i < tokensLength; i++) {
				if(types[i] == WildcardPattern.LITERAL_TYPE ||
						types[i] == WildcardPattern.PLUS_TYPE ||
						types[i] == WildcardPattern.SEPARATOR_TYPE) {
					return false;
				}
			}
		}
		
		return true;
	}

}
