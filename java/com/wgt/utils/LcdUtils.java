package com.wgt.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.phoid.util.Base64;

public class LcdUtils {

    public static String ellipsisTrunc(String inStr, int maxLen) {
        return ((inStr == null) || (inStr.length() <= maxLen)) ? inStr : inStr.substring(0,maxLen-3)+"..." ;
    }
    
    public static String ellipsisTruncMid(String inStr, int maxLen) {
        if((inStr == null) || (inStr.length() <= maxLen) || (maxLen < 4)) {
        	return inStr ;
        } else {
        	int headLen = (maxLen-2) / 2 ;
        	return inStr.substring(0, headLen)+".."+inStr.substring(inStr.length() - (maxLen - 2 - headLen)) ;
        }
    }

    public static String toHash(String clearStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1" );
            md.update(clearStr.getBytes("UTF-8")) ;
            String str = Base64.encode(md.digest()) ;
            return "{SHA}"+str ;
        } catch (NoSuchAlgorithmException xx) {
            throw new RuntimeException(xx) ;
        } catch (UnsupportedEncodingException xx) {
            throw new RuntimeException(xx) ;
        }
    }
    public static boolean equalObjects(Object o1, Object o2) {   
        return o1 == null? o2 == null : o1.equals(o2) ;
    }
    public static boolean isEmptyOrNull(String str) {
        return str==null ? true : str.length() < 1 ;
    }
    public static boolean isBlanksOrNull(String str) {
        return str==null ? true : str.trim().length() < 1 ;
    }
    public static String emptyIfNull(Object obj) {
        return (obj == null) ? "" : emptyIfNullString(obj.toString()) ;
    }
    public static String emptyIfNullString(String str) {
        return (str == null) ? "" : str ;
    }
    public static boolean isEmptyOrNull(List lst) {
        return lst==null ? true : lst.size() < 1 ;
    }
    
    public static <T> boolean isEmptyOrNull(T[] a) {
        return a == null ? true : a.length == 0;
    }

    public static boolean getOptionalBoolVal(Boolean obj, boolean defaultVal) {
        return (obj == null) ? defaultVal : obj.booleanValue() ;
    }
    
    public static String toStringSafe(Object src) {
    	return src==null? null: src.toString() ;
    }
    
    public static String safeTrim(String val) {
    	return val != null ? val.trim() : val;
    }
    
    public static String safeTrim(String val, String defaultVal) {
    	return val != null ? val.trim() : defaultVal ;
    }

    public static Integer defaultIfNull(Integer obj, int defaultVal) {
        return (obj == null) ? defaultVal : obj ;
    }

    public static String getPrefix(String val, char target) {
        if (val == null) {
            return val ;
        } else {
            int inx = val.indexOf(target) ;
            return (inx < 0) ? val : val.substring(0,inx) ;
        }
    }
    
    public static String getSuffix(String val, char target) {
        if (val == null) {
            return val ;
        } else {
            int inx = val.lastIndexOf(target) ;
            return (inx < 0) ? val : val.substring(inx) ;
        }
    }
    
    public static String flattenCollection(Collection<String> inStrs) {
        StringBuffer sb = new StringBuffer() ;
        for (String str : inStrs) {
            if (str == null) {
                sb.append("2-1") ;
                continue ;
            }
            String strLen = String.valueOf(str.length()) ;
            String strLenLen = String.valueOf(strLen.length()) ;
            if (strLenLen.length() > 1) {
                throw new RuntimeException("Cannot encode element of length "+strLen) ;
            }
            sb.append(String.valueOf(strLenLen)).append(strLen).append(str) ;
        }
        return sb.toString() ;
    }
    
    public static String flattenCollection(String... inStrs) {
        StringBuffer sb = new StringBuffer() ;
        for (String str : inStrs) {
            if (str == null) {
                sb.append("2-1") ;
                continue ;
            }
            String strLen = String.valueOf(str.length()) ;
            String strLenLen = String.valueOf(strLen.length()) ;
            if (strLenLen.length() > 1) {
                throw new RuntimeException("Cannot encode element of length "+strLen) ;
            }
            sb.append(String.valueOf(strLenLen)).append(strLen).append(str) ;
        }
        return sb.toString() ;
    }
    
    public static List<String> decodeFlatList(String inStr) {
        List<String> ret = new ArrayList<String>() ;
        if (inStr == null) {
            return ret ;
        }
        int startInx = 0 ;
        try {
            while (startInx < inStr.length()) {
                int segLenLen = Integer.valueOf(inStr.substring(startInx,startInx+1)) ;
                startInx++ ;
                int segLen = Integer.valueOf(inStr.substring(startInx,startInx+segLenLen)) ;
                startInx += segLenLen ;
                if (segLen < 0) {
                    ret.add(null) ;
                    continue ;
                }
                ret.add(inStr.substring(startInx, startInx+segLen)) ;
                startInx += segLen ;
            }
        } catch (Exception xx) {
            throw new RuntimeException("Malformed Coll<String> encoding, ofst="+startInx+", Str=["+inStr+"]",xx) ;
        }
        return ret ;
    }
    
	private static final Pattern CRLF_PATT = Pattern.compile("\r\n", Pattern.MULTILINE) ;

	public static String removeCRLF(String s) {
    	// conditionally sanitize
    	return (s != null) && CRLF_PATT.matcher(s).find() ? CRLF_PATT.matcher(s).replaceAll("") : s;
    }

    public static String sanitizeCRLF(String s) {
    	// conditionally sanitize
    	return s != null ? s.replaceAll("\r\n", "\\\\r\\\\n") : s;
    }

    private final static Pattern DHMS_PATTERN = Pattern.compile("((\\d+)d)?((\\d+)h)?((\\d+)m)?((\\d+)s)?",Pattern.CASE_INSENSITIVE) ;

    private static long toLong(String decStr) {
    	return (decStr == null) ? 0 : Long.parseLong(decStr) ;
    }

	public static long fromHmstime(String value, long dflt) {
		if (isBlanksOrNull(value)) {
			return dflt ;
		}
		Matcher matcher = DHMS_PATTERN.matcher(value) ;
		if (!matcher.matches()) {
			throw new RuntimeException("Value '"+value+"' is not of pattern NNdNNhNNmNNs") ;
		}
		MatchResult rslt = matcher.toMatchResult() ;
		return (toLong(rslt.group(2))*24*3600)+(toLong(rslt.group(4))*3600) + (toLong(rslt.group(6))*60) + toLong(rslt.group(8)) ;
	}
	
	public static String escapeHtml(String s) {
		return s; //TODO for VERA Code
	}

	public static String toJvmRef(Object o) {
		return Integer.toString(System.identityHashCode(o),16) ;
	}

	public static <T> String dumpColl(String leg, Collection<T> coll) {
		return leg+"="+toDump(coll) ;
	}
	public static <T> String toDump(Collection<T> coll) {
	    StringBuffer sb = new StringBuffer() ;
	    if (coll == null) {
	    	sb.append("nool") ;
	    } else {
		    sb.append("{") ;
		    for (T item:coll) {
		        sb.append(item.toString()).append(",") ;
		    }
		    sb.append("}") ;
	    }
	    return sb.toString() ;
	}
	public static <T> String toDump(T[] coll) {
	    StringBuffer sb = new StringBuffer() ;
	    if (coll == null) {
	    	sb.append("nool") ;
	    } else {
		    sb.append("{") ;
		    for (T item:coll) {
		        sb.append(item.toString()).append(",") ;
		    }
		    sb.append("}") ;
	    }
	    return sb.toString() ;
	}
	
	public static <T> String toDelimitedList(Collection<T> coll, String delim) {
	    if ((coll == null) || coll.isEmpty()) {
	    	return "" ;
	    } else {
		    StringBuffer sb = new StringBuffer() ;
		    for (T item:coll) {
		        sb.append(delim).append(item.toString()) ;
		    }
		    return sb.substring(delim.length()) ;
	    }
	}
	
	public static String getDefaultIfEmpty(String src, String dflt) {
		return isBlanksOrNull(src) ? dflt : src ;
	}
	public static boolean getDefaultIfEmpty(String src, boolean dflt) {
		if (isBlanksOrNull(src)) {
			return dflt ;
		} else {
			return Boolean.parseBoolean(src) ;
		}
	}
	public static int getDefaultIfEmpty(String src, int dflt) {
		if (isBlanksOrNull(src)) {
			return dflt ;
		} else {
			try {
				return Integer.parseInt(src) ;
			} catch (NumberFormatException xx) {
				return dflt ;
			}
		}
	}
	
	public static StringBuffer appendCnt(StringBuffer sb, int cnt, String fill, String delim) {
		if (sb == null) {
			sb = new StringBuffer() ;
		}
        while (cnt-- > 0) {
            sb.append(fill) ;
            if (cnt > 0) {
            	sb.append(delim) ;
            }
        }
        return sb ;
	}

	public static String appendCnt(int cnt, String fill, String delim) {
		return appendCnt(null, cnt, fill, delim).toString() ;
	}

	public static String limitedLenTrim(String str, int maxLen) {
	    return (str != null ? limitedLen(str.trim(),maxLen) : str) ;	    
	}
	
	public static String limitedLen(String str, int maxLen) {
	    return ((str != null) && (str.length() > maxLen)) ? str.substring(0, maxLen) : str ;	    
	}

	public static String nullIfEmptyLimitedTrim(String val, int maxLen) {
		return (val != null ? nullIfEmptyLimited(val.trim(),maxLen) : null) ;
	}
	
	public static String nullIfEmptyLimited(String val, int maxLen) {
	    return ((val != null) && (val.length() > 0)) ? (val.length() > maxLen? val.substring(0,maxLen) : val) : null ;
	}
}
