package com.xy.shuhua.util;

import android.content.SharedPreferences;
import com.xy.shuhua.ui.CustomApplication;

import java.util.*;

/**
 * Created by xiaoyu on 2016/4/13.
 */
public class SharedPreferenceUtil {
    /*
	 * the default mode, where the created file can only be accessed by the calling application
	 * (or all applications sharing the same user ID).
	 */
    public static final int MODE_PRIVATE = 0;
    /*
     * allow all other applications to have read access to the created file
     * This constant was deprecated in API level 17.
     */
    public static final int MODE_WORLD_READABLE = 1;
    /*
     * allow all other applications to have write access to the created file.
     * This constant was deprecated in API level 17.
     */
    public static final int MODE_WORLD_WRITEABLE = 2;

    /**
     * ��һ��String���ݴ��뵽������
     * @param spName ���������
     * @param keyStr Ҫ���뻺���е�key
     * @param valueStr Ҫ���뻺���е�value
     */
    public static void setStringDataIntoSP(String spName, String keyStr, String valueStr) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        sp.edit().putString(keyStr, valueStr).commit();
    }

    /**
     * ��һ��Boolean���ݴ��뵽������
     * @param spName ���������
     * @param keyStr Ҫ���뻺���е�key
     * @param valueStr Ҫ���뻺���е�value
     */
    public static void setBooleanDataIntoSP(String spName, String keyStr, Boolean valueStr) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        sp.edit().putBoolean(keyStr, valueStr).commit();
    }

    /**
     * ��һ��Int���ݴ��뵽������
     * @param spName ���������
     * @param keyStr Ҫ���뻺���е�key
     * @param valueStr Ҫ���뻺���е�value
     */
    public static void setIntDataIntoSP(String spName, String keyStr, int valueStr) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        sp.edit().putInt(keyStr, valueStr).commit();
    }

    /**
     * ��һ��Float���ݴ��뵽������
     * @param spName ���������
     * @param keyStr Ҫ���뻺���е�key
     * @param valueStr Ҫ���뻺���е�value
     */
    public static void setFloatDataIntoSP(String spName, String keyStr, Float valueStr) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        sp.edit().putFloat(keyStr, valueStr).commit();
    }

    /**
     * ��һ��Long���ݴ��뵽������
     * @param spName ���������
     * @param keyStr Ҫ���뻺���е�key
     * @param valueStr Ҫ���뻺���е�value
     */
    public static void setLongDataIntoSP(String spName, String keyStr, Long valueStr) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        sp.edit().putLong(keyStr, valueStr).commit();
    }

    /**
     * ��һϵ��String���ݴ��뵽������
     * @param spName ���������
     * @param keyStr Ҫ���뻺���е�key
     * @param valueStringSet һϵ�е�String����
     */
//    public static void setStringSetDataIntoSP(String spName, String keyStr, Set<String> valueStringSet) {
//    	if(Build.VERSION.SDK_INT >= 11) {
//	    	SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences(spName, MODE_PRIVATE);
//	    	sp.edit().putStringSet(keyStr, valueStringSet).commit();
//    	}
//    }

    /**
     * ��ȡ�����е�һ��String����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @return �����ж�Ӧ����key��value
     */
    public static String getStringValueFromSP(String spName, String keyStr) {
        return getStringValueFromSP(spName, keyStr, "");
    }

    /**
     * ��ȡ�����е�һ��String����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @param defaultValue �����ж�Ӧ����key��Ĭ��ֵ
     * @return �����ж�Ӧ����key��value
     */
    public static String getStringValueFromSP(String spName, String keyStr, String defaultValue) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getString(keyStr, defaultValue);
    }

    /**
     * ��ȡ�����е�һ��Float����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @return �����ж�Ӧ����key��value
     */
    public static Float getFloatValueFromSP(String spName, String keyStr) {
        return getFloatValueFromSP(spName, keyStr, 0.0f);
    }

    /**
     * ��ȡ�����е�һ��Float����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @param defaultValue �����ж�Ӧ����key��Ĭ��ֵ
     * @return �����ж�Ӧ����key��value
     */
    public static Float getFloatValueFromSP(String spName, String keyStr, Float defaultValue) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getFloat(keyStr, defaultValue);
    }

    /**
     * ��ȡ�����е�һ��Int����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @return �����ж�Ӧ����key��value
     */
    public static int getIntValueFromSP(String spName, String keyStr) {
        return getIntValueFromSP(spName, keyStr, 0);
    }

    /**
     * ��ȡ�����е�һ��Int����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @param defaultValue �����ж�Ӧ����key��Ĭ��ֵ
     * @return �����ж�Ӧ����key��value
     */
    public static int getIntValueFromSP(String spName, String keyStr, int defaultValue) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getInt(keyStr, defaultValue);
    }

    /**
     * ��ȡ�����е�һ��Boolean����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @return �����ж�Ӧ����key��value
     */
    public static boolean getBooleanValueFromSP(String spName, String keyStr) {
        return getBooleanValueFromSP(spName, keyStr, false);
    }

    /**
     * ��ȡ�����е�һ��Boolean����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @param defaultValue �����ж�Ӧ����key��Ĭ��ֵ
     * @return �����ж�Ӧ����key��value
     */
    public static boolean getBooleanValueFromSP(String spName, String keyStr, Boolean defaultValue) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getBoolean(keyStr, defaultValue);
    }

    /**
     * ��ȡ�����е�һ��Long����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @return �����ж�Ӧ����key��value
     */
    public static Long getLongValueFromSP(String spName, String keyStr) {
        return getLongValueFromSP(spName, keyStr, 0l);
    }

    /**
     * ��ȡ�����е�һ��Long����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @param defaultValue �����ж�Ӧ����key��Ĭ��ֵ
     * @return �����ж�Ӧ����key��value
     */
    public static Long getLongValueFromSP(String spName, String keyStr, Long defaultValue) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getLong(keyStr, 0l);
    }

    /**
     * ��ȡ�����е�һϵ��String����
     * @param spName ���������
     * @param keyStr �Ѵ��뻺���е�key
     * @return �����ж�Ӧ����key��һϵ��Stringֵ
     */
//    public static Set<String> getStringSetValueFromSP(String spName, String keyStr) {
//    	Set<String> sets = new HashSet<String>();
//    	if(Build.VERSION.SDK_INT >= 11) {
//    		SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences(spName, MODE_PRIVATE);
//    		sets = sp.getStringSet(keyStr, null);
//    	}
//    	return sets;
//    }

    /**
     * ����ֵ�����飬���뵽������
     * @param spName ���������
     * @param keyValueMap Ҫ���뻺���еļ�ֵ��
     */
    public static void setDataIntoSP(String spName, HashMap<String, Object> keyValueMap) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(keyValueMap != null && !keyValueMap.isEmpty()) {
            Set<String> keySet = keyValueMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = keyValueMap.get(key);
                if(value.getClass() == String.class) {
                    editor.putString(key, (String)value);
                } else if(value.getClass() == Integer.class) {
                    editor.putInt(key, (Integer)value);
                } else if(value.getClass() == Boolean.class) {
                    editor.putBoolean(key, (Boolean)value);
                } else if(value.getClass() == Long.class) {
                    editor.putLong(key, (Long)value);
                } else if(value.getClass() == Float.class) {
                    editor.putFloat(key, (Float)value);
                }
            }
            editor.commit();
        }
    }

    /**
     * ��ȡ���keyֵ��Ӧ��values
     * @param spName ���������
     * @param keyValueMap Ҫ��ȡ�Ļ����е�keyֵ
     * @return
     */
    public static List<Object> getValuesFromSP(String spName, HashMap<String, Object> keyValueMap) {
        List<Object> values = new ArrayList<Object>();
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        if(keyValueMap != null && !keyValueMap.isEmpty()) {
            Set<String> keySet = keyValueMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = keyValueMap.get(key);
                if(value == String.class) {
                    values.add(sp.getString(key, ""));
                } else if(value == Integer.class) {
                    values.add(sp.getInt(key, 0));
                } else if(value == Boolean.class) {
                    values.add(sp.getBoolean(key, false));
                } else if(value == Long.class) {
                    values.add(sp.getLong(key, 0l));
                } else if(value == Float.class) {
                    values.add(sp.getFloat(key, 0.0f));
                }
            }
        }
        return values;
    }

    /**
     * ��ȡ���������е�����
     * @param spName ���������
     * @return
     */
    public static Map<String, ?> getAllFromSP(String spName) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * ��֤�������Ƿ��Ѿ���ĳ��keyֵ
     * @param spName ���������
     * @param key Ҫ��ѯ��keyֵ
     * @return
     */
    public static boolean hasKeyInSP(String spName, String key) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * ɾ�������е�ĳ����ֵ��
     * @param spName ���������
     * @param key ��ɾ���Ļ����е�keyֵ
     */
    public static void deleteValueInSP(String spName, String key) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        if(sp.contains(key)) {
            sp.edit().remove(key).commit();
        }
    }

    /**
     * ɾ�������е�����ֵ
     * @param spName ���������
     */
    public static void deleteAllInSP(String spName) {
        SharedPreferences sp = CustomApplication.getInstance().getSharedPreferences(spName, MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
