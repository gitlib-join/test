package com.ssic.zk.base_util;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkUtil {

	public static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);

	private ZooKeeper zk = null;

	public ZkUtil(String url, int sessionTimeout) {
		try {
			zk = new ZooKeeper(url, sessionTimeout, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 创建zNode节点, String create(path<节点路径>, data[]<节点内容>, List(ACL访问控制列表),
	 * CreateMode<zNode创建类型>)
	 * </p>
	 * <br/>
	 * 
	 * <pre>
	 *     节点创建类型(CreateMode)
	 *     1、PERSISTENT:持久化节点
	 *     2、PERSISTENT_SEQUENTIAL:顺序自动编号持久化节点，这种节点会根据当前已存在的节点数自动加 1
	 *     3、EPHEMERAL:临时节点客户端,session超时这类节点就会被自动删除
	 *     4、EPHEMERAL_SEQUENTIAL:临时自动编号节点
	 * </pre>
	 * 
	 * @param path
	 *            zNode节点路径
	 * @param data
	 *            zNode数据内容
	 * @return 创建成功返回true, 反之返回false.
	 */
	public boolean createPath(String path, String data) {
		try {
			String zkPath = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("节点创建成功, Path: " + zkPath + ", content: " + data);
			return true;
		} catch (KeeperException e) {
			logger.error("节点创建失败, 发生KeeperException! path: " + path + ", data:" + data + ", errMsg:" + e.getMessage(),
					e);
		} catch (InterruptedException e) {
			logger.error(
					"节点创建失败, 发生 InterruptedException! path: " + path + ", data:" + data + ", errMsg:" + e.getMessage(),
					e);
		}
		return false;
	}

	/**
	 * <p>
	 * 删除一个zMode节点, void delete(path<节点路径>, stat<数据版本号>)
	 * </p>
	 * <br/>
	 * 
	 * <pre>
	 *     说明
	 *     1、版本号不一致,无法进行数据删除操作.
	 *     2、如果版本号与znode的版本号不一致,将无法删除,是一种乐观加锁机制;如果将版本号设置为-1,不会去检测版本,直接删除.
	 * </pre>
	 * 
	 * @param path
	 *            zNode节点路径
	 * @return 删除成功返回true,反之返回false.
	 */
	public boolean deletePath(String path) {
		try {
			zk.delete(path, -1);
			logger.info("节点删除成功, Path: " + path);
			return true;
		} catch (KeeperException e) {
			logger.error("节点删除失败, 发生KeeperException! path: " + path + ", errMsg:" + e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error("节点删除失败, 发生 InterruptedException! path: " + path + ", errMsg:" + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * <p>
	 * 更新指定节点数据内容, Stat setData(path<节点路径>, data[]<节点内容>, stat<数据版本号>)
	 * </p>
	 * 
	 * <pre>
	 *     设置某个znode上的数据时如果为-1，跳过版本检查
	 * </pre>
	 * 
	 * @param path
	 *            zNode节点路径
	 * @param data
	 *            zNode数据内容
	 * @return 更新成功返回true,返回返回false
	 */
	public boolean writeData(String path, String data) {
		try {
			Stat stat = zk.setData(path, data.getBytes(), -1);
			logger.info("更新数据成功, path：" + path + ", stat: " + stat);
			return true;
		} catch (KeeperException e) {
			logger.error("更新数据失败, 发生KeeperException! path: " + path + ", data:" + data + ", errMsg:" + e.getMessage(),
					e);
		} catch (InterruptedException e) {
			logger.error(
					"更新数据失败, 发生InterruptedException! path: " + path + ", data:" + data + ", errMsg:" + e.getMessage(),
					e);
		}
		return false;
	}

	/**
	 * <p>
	 * 读取指定节点数据内容,byte[] getData(path<节点路径>, watcher<监视器>, stat<数据版本号>)
	 * </p>
	 * 
	 * @param path
	 *            zNode节点路径
	 * @return 节点存储的值,有值返回,无值返回null
	 */
	public String readData(String path) {
		String data = null;
		try {
			data = new String(zk.getData(path, false, null));
			logger.info("读取数据成功, path:" + path + ", content:" + data);
		} catch (KeeperException e) {
			logger.error("读取数据失败,发生KeeperException! path: " + path + ", errMsg:" + e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error("读取数据失败,发生InterruptedException! path: " + path + ", errMsg:" + e.getMessage(), e);
		}
		return data;
	}

	/**
	 * <p>
	 * 获取某个节点下的所有子节点,List getChildren(path<节点路径>, watcher<监视器>)该方法有多个重载
	 * </p>
	 * 
	 * @param path
	 *            zNode节点路径
	 * @return 子节点路径集合 说明,这里返回的值为节点名
	 * 
	 *         <pre>
	 *     eg.
	 *     /node
	 *     /node/child1
	 *     /node/child2
	 *     getChild( "node" )户的集合中的值为["child1","child2"]
	 *         </pre>
	 *
	 *
	 *
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChild(String path) {
		try {
			List<String> list = zk.getChildren(path, false);
			if (list.isEmpty()) {
				logger.info("中没有节点" + path);
			}
			return list;
		} catch (KeeperException e) {
			logger.error("读取子节点数据失败,发生KeeperException! path: " + path + ", errMsg:" + e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error("读取子节点数据失败,发生InterruptedException! path: " + path + ", errMsg:" + e.getMessage(), e);
		}
		return null;
	}

	public String getData(String path) {
		try {
			byte[] byt = zk.getData(path, null, null);
			return new String(byt);
		} catch (KeeperException e) {
			logger.error("读取数据失败,发生KeeperException! path: " + path + ", errMsg:" + e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error("读取数据失败,发生InterruptedException! path: " + path + ", errMsg:" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * <p>
	 * 判断某个zNode节点是否存在, Stat exists(path<节点路径>, watch<并设置是否监控这个目录节点，这里的 watcher 是在创建
	 * ZooKeeper 实例时指定的 watcher>)
	 * </p>
	 * 
	 * @param path
	 *            zNode节点路径
	 * @return 存在返回true,反之返回false
	 */
	public boolean isExists(String path) {
		try {
			Stat stat = zk.exists(path, false);
			return null != stat;
		} catch (KeeperException e) {
			logger.error("读取数据失败,发生KeeperException! path: " + path + ", errMsg:" + e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error("读取数据失败,发生InterruptedException! path: " + path + ", errMsg:" + e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 关闭ZK连接
	 */
	public void releaseConnection() {
		if (null != zk) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				logger.error("release connection error ," + e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) {

	}

}