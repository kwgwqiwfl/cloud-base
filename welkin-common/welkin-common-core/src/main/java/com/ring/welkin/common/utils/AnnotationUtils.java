package com.ring.welkin.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by chunyali on 17/5/10.
 */
@Slf4j
public class AnnotationUtils {
	/**
	 * 从包package中获取所有的Class
	 *
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack) {

		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread()
						 .getContextClassLoader()
						 .getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					log.debug("Scan file type, path = " + filePath);
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						log.debug("Scan jar file:" + jar.getName());
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							if (name.charAt(0) == '/') {
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx)
													  .replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class.forName(packageName + '.' + className));
										} catch (ClassNotFoundException e) {
											log.error(e.getMessage());
										}
									}
								}
							}
						}
					} catch (IOException e) {
						log.error("Error getting file from jar package.");
						log.error(e.getMessage());
					}
				}
			}
		} catch (IOException e) {
			log.error("get classes error: ", e);
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
														Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName()
																 .endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
					classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName()
									   .substring(0, file.getName()
														 .length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' + className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(
						Thread.currentThread()
							  .getContextClassLoader()
							  .loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					log.error("Add user defined view class error, '.class' file not found for this class.");
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<Class> getClassesForPackage(String pckgname) throws ClassNotFoundException {
		// There may be more than one if a package is split over multiple jars/paths
		List<Class> classes = new ArrayList<Class>();
		String className = "";
		try {
			ClassLoader cld = Thread.currentThread()
									.getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}

			String pckgnmePathStr = pckgname.replace('.', '/');
			// Ask for all resources for the path
			Enumeration<URL> resources = cld.getResources(pckgnmePathStr);

			while (resources.hasMoreElements()) {
				URL res = resources.nextElement();
				if (res.getProtocol()
					   .equalsIgnoreCase("jar")) {
					JarURLConnection conn = (JarURLConnection) res.openConnection();
					JarFile jar = conn.getJarFile();
					for (JarEntry e : Collections.list(jar.entries())) {

						if (e.getName()
							 .startsWith(pckgnmePathStr) && e.getName()
															 .endsWith(".class")
							&& !e.getName()
								 .contains("$")) {
							String name = e.getName()
										   .substring(pckgnmePathStr.length() + 1);

							// Do not discover classes in the sub-package
							if (name.indexOf('/') > -1)
								continue;
							className = e.getName()
										 .replace("/", ".")
										 .substring(0, e.getName()
														.length() - 6);
							try {
								classes.add(Class.forName(className));
							} catch (NoClassDefFoundError ncdfe) {
							}
						}
					}
				}
			}
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(
				pckgname + " does not appear to be " + "a valid package (Null pointer exception)");
		} catch (UnsupportedEncodingException encex) {
			throw new ClassNotFoundException(
				pckgname + " does not appear to be " + "a valid package (Unsupported encoding)");
		} catch (IOException ioex) {
			throw new ClassNotFoundException(
				"IOException was thrown when trying " + "to get all resources for " + pckgname, ioex);
		} catch (Throwable te) {
			throw new ClassNotFoundException(
				"IOException was thrown when trying " + "to get all resources for " + pckgname + className, te);
		}

		return classes;
	}

	/**
	 * @param classes : list of classes for scanning
	 * @param clazz   : Annotation class used
	 * @return all classes using the annotation specified
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Map<String, Class<?>> scannAllClassesForAnnotation(Set<Class<?>> classes, Class<?> clazz) {

		HashMap<String, Class<?>> classMap = new HashMap<String, Class<?>>();

		for (Class clz : classes) {
			Annotation annotation = clz.getAnnotation(clazz);
			if (annotation != null) {
				// annotation;
				classMap.put(clz.getName(), clz);
			}
		}
		return classMap;
	}
}
