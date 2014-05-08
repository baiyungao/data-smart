package com.washingtongt.access;

import java.security.SecureRandom;

public class PasscodeFactory {

	public static final int PASSCODE_TYPE_NUMBER = 0;
        public static final int PASSCODE_TYPE_LETTER = 1;
        public static final int PASSCODE_TYPE_NUMBER_LETTER = 2;
        public static final int PASSCODE_TYPE_FULL_CHART = 3;

	static final char[] NUMBER_CHAR = { '0',
	        '1','2','3','4','5','6','7','8',
	        '9'};

	static final char[] LETTER_CHAR = { 'A','B',
	        'C','D','E','F','G','H','I','J',
	        'K','L','M','N','O','P','Q','R',
	        'S','T','U','V','W','X','Y','Z',
	        'a','b',
	        'c','d','e','f','g','h','i','j',
	        'k','l','m','n','o','p','q','r',
	        's','t','u','v','w','x','y','z'};

	static final char[] NUMBER_LETTER_CHAR = {'0',
	        '1','2','3','4','5','6','7','8',
                '9','A','B','C','D','E','F','G','H','I','J',
                'K','L','M','N','O','P','Q','R',
	        'S','T','U','V','W','X','Y','Z',
	        'a','b',
	        'c','d','e','f','g','h','i','j',
	        'k','l','m','n','o','p','q','r',
	        's','t','u','v','w','x','y','z'};

	static final char[] VALID_PASSWORD_CHAR = {
	        '!','#','$','%','&','(',
	        ')','*','+','-','0',
	        '1','2','3','4','5','6','7','8',
	        '9',':',';','<','?','@','A','B',
	        'C','D','E','F','G','H','I','J',
	        'K','L','M','N','O','P','Q','R',
	        'S','T','U','V','W','X','Y','Z',
	        '[',']','_','a','b',
	        'c','d','e','f','g','h','i','j',
	        'k','l','m','n','o','p','q','r',
	        's','t','u','v','w','x','y','z',
	        '{','|','}','~'
	    };

        /**
         *
         * @param length
         * @return
         * @throws Exception
         */
	public static String generatePasscode(int length) throws Exception {

          return  generatePasscode(length,PASSCODE_TYPE_FULL_CHART) ;
            }

	public static String generatePasscode(int length,int type) throws Exception {
          char[] charSet = null;
          switch (type)
          {
            case  PASSCODE_TYPE_NUMBER:
                charSet= NUMBER_CHAR;
                break;
            case  PASSCODE_TYPE_LETTER:
                charSet= LETTER_CHAR;
                break;
            case PASSCODE_TYPE_NUMBER_LETTER:
                charSet= NUMBER_LETTER_CHAR;
                break;
            default:
                charSet= VALID_PASSWORD_CHAR;

          }
          SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG") ;
          byte[] seed = rnd.generateSeed(length) ;
          StringBuffer sb = new StringBuffer() ;
          for (int i = 0 ; i < seed.length; i++) {
              sb.append(charSet[((seed[i] & 255)%(charSet.length))]) ;
          }
          return sb.toString() ;
            }

}
