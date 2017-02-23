/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.airavata.sga.data.staging.task.protocols.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.exception.SSHException;
import org.apache.airavata.sga.data.staging.task.protocols.RemoteInteraction;
import org.apache.airavata.sga.data.staging.task.util.Factory;
import org.apache.airavata.sga.data.staging.task.util.StandardOutReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Utility class to do all ssh and scp related things.
 */
public class SSHImpl implements RemoteInteraction {

	private static final Logger logger = LoggerFactory.getLogger(SSHImpl.class);

	private Session session = null;

	private Session srcSession = null;

	public SSHImpl(ServerInfo serverInfo) throws JSchException {
		session = Factory.getSSHSession(serverInfo);
	}

	public SSHImpl(ServerInfo srcServerInfo, ServerInfo destServerInfo) throws JSchException {
		srcSession = Factory.getSSHSession(srcServerInfo);
		session = Factory.getSSHSession(destServerInfo);
	}

	/**
	 * This will copy a local file to a remote location
	 *
	 * @param remoteFile remote location you want to transfer the file, this cannot be a directory, if user pass
	 *                   a dirctory we do copy it to that directory but we simply return the directory name
	 *                   todo handle the directory name as input and return the proper final output file name
	 * @param localFile  Local file to transfer, this can be a directory
	 * @return returns the final remote file path, so that users can use the new file location
	 */
	public String write(String localFile, String remoteFile) throws SSHException{

		logger.info("write() -> Transferring file. From  : " + localFile + ", To : " + remoteFile);
		Channel channel = null;
		OutputStream out = null;
		InputStream in = null;
		try{
			FileInputStream fis = null;
			String prefix = null;
			if (new File(localFile).isDirectory()) {
				prefix = localFile + File.separator;
			}
			boolean ptimestamp = true;
			if(null != session && !session.isConnected()){
				session.connect();
			}
			// exec 'scp -t rfile' remotely
			String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remoteFile;
			channel = session.openChannel("exec");

			StandardOutReader stdOutReader = new StandardOutReader();
			((ChannelExec) channel).setErrStream(stdOutReader.getStandardError());
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			out = channel.getOutputStream();
			in = channel.getInputStream();

			channel.connect();

			if (checkAck(in) != 0) {
				String error = "Error Reading input Stream";
				logger.error(error);
				throw new SSHException(error);
			}

			File _lfile = new File(localFile);

			if (ptimestamp) {
				command = "T" + (_lfile.lastModified() / 1000) + " 0";
				// The access time should be sent here,
				// but it is not accessible with JavaAPI ;-<
				command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					String error = "Error Reading input Stream";
					logger.error(error);
					throw new SSHException(error);
				}
			}

			// send "C0644 filesize filename", where filename should not include '/'
			long filesize = _lfile.length();
			command = "C0644 " + filesize + " ";
			if (localFile.lastIndexOf('/') > 0) {
				command += localFile.substring(localFile.lastIndexOf('/') + 1);
			} else {
				command += localFile;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				String error = "Error Reading input Stream";
				logger.error(error);
				throw new SSHException(error);
			}

			// send a content of localFile
			fis = new FileInputStream(localFile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0) break;
				out.write(buf, 0, len); //out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				String error = "Error Reading input Stream";
				logger.error(error);
				throw new SSHException(error);
			}
			out.close();
			stdOutReader.onOutput(channel);



			if (stdOutReader.getStdErrorString().contains("scp:")) {
				throw new SSHException(stdOutReader.getStdErrorString());
			}
			//since remote file is always a file  we just return the file
			return remoteFile;
		}catch (JSchException | IOException e){
			logger.error("write() -> Error transferring file. From  : " + localFile + ", To : " + remoteFile, e);
			throw new SSHException("Error transferring file. From  : " + localFile + ", To : " + remoteFile);
		} finally {
			try{
				if( null != in) in.close();
				if( null != out) out.close();
			}catch (IOException e){
				logger.error("write() -> Error closing stream. From  : " + localFile + ", To : " + remoteFile, e);
			}

			if( null != channel && channel.isConnected( ))channel.disconnect();

			if( null != session && session.isConnected() ){
				logger.info("write() -> Terminating session. From  : " + localFile + ", To : " + remoteFile);
				session.disconnect();
			}
		}

	}

	/**
	 * This method will copy a remote file to a local directory
	 *
	 * @param remoteFile remote file path, this has to be a full qualified path
	 * @param localFile  This is the local file to copy, this can be a directory too
	 * @return returns the final local file path of the new file came from the remote resource
	 */
	public String read(String remoteFile, String localFile) throws SSHException {

		logger.info("read() -> Transferring file. From  : " + localFile + ", To : " + remoteFile);

		FileOutputStream fos = null;
		Channel channel = null;

		try {
			String prefix = null;
			if (new File(localFile).isDirectory()) {
				prefix = localFile + File.separator;
			}
			if(null != session && !session.isConnected()){
				session.connect();
			}
			// exec 'scp -f remotefile' remotely
			String command = "scp -f " + remoteFile;
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			StandardOutReader stdOutReader = new StandardOutReader();
			((ChannelExec) channel).setErrStream(stdOutReader.getStandardError());
			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

            if (!channel.isClosed()){
                channel.connect();
            }

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ') break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				String file = null;
				for (int i = 0; ; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				//System.out.println("filesize="+filesize+", file="+file);

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				// read a content of lfile
				fos = new FileOutputStream(prefix == null ? localFile : prefix + file);
				int foo;
				while (true) {
					if (buf.length < filesize) foo = buf.length;
					else foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L) break;
				}
				fos.close();
				fos = null;

				if (checkAck(in) != 0) {
					String error = "Error transfering the file content";
					logger.error(error);
					throw new SSHException(error);
				}

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
			}
			stdOutReader.onOutput(channel);
			if (stdOutReader.getStdErrorString().contains("scp:")) {
				throw new SSHException(stdOutReader.getStdErrorString());
			}

		} catch (JSchException | IOException e){
			logger.error("read() -> Error transferring file. From  : " + localFile + ", To : " + remoteFile, e);
			throw new SSHException("Error transferring file. From  : " + localFile + ", To : " + remoteFile);

		}finally {

			if (null != channel && channel.isConnected()) {
				channel.disconnect();
			}

			if( null != session && session.isConnected() ){
				logger.info("read() -> Terminating session. From  : " + localFile + ", To : " + remoteFile);
				session.disconnect();
			}
			try {
				if (fos != null) fos.close();
			} catch (Exception ee) {
			}
		}
		return remoteFile;
	}

	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		if (b == 0) return b;
		if (b == -1) return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			}
			while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}

	/**
	 * This method will copy a remote file to a local directory
	 *
	 * @param sourceFile remote file path, this has to be a full qualified path
	 * @param destinationFile This is the local file to copy, this can be a directory too
	 * @return returns the final local file path of the new file came from the remote resource
	 */
	public String thirdPartyTransfer(String sourceFile, String destinationFile, boolean ignoreEmptyFile) throws SSHException {
		OutputStream sout = null;
		InputStream sin = null;
		OutputStream dout = null;
		InputStream din = null;
		Channel sourceChannel = null;
		Channel targetChannel = null;
		try {
			String prefix = null;

			// exec 'scp -f sourceFile'
			String sourceCommand = "scp -f " + sourceFile;
			if(null != srcSession && !srcSession.isConnected()){
				srcSession.connect();
			}
			sourceChannel = srcSession.openChannel("exec");
			((ChannelExec) sourceChannel).setCommand(sourceCommand);
			StandardOutReader sourceStdOutReader = new StandardOutReader();
			((ChannelExec) sourceChannel).setErrStream(sourceStdOutReader.getStandardError());
			// get I/O streams for remote scp
			sout = sourceChannel.getOutputStream();
			sin = sourceChannel.getInputStream();
			sourceChannel.connect();


			boolean ptimestamp = true;
			// exec 'scp -t destinationFile'
			String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + destinationFile;
			if(null != session && !session.isConnected()){
				session.connect();
			}
			targetChannel = session.openChannel("exec");
			StandardOutReader targetStdOutReader = new StandardOutReader();
			((ChannelExec) targetChannel).setErrStream(targetStdOutReader.getStandardError());
			((ChannelExec) targetChannel).setCommand(command);
			// get I/O streams for remote scp
			dout = targetChannel.getOutputStream();
			din = targetChannel.getInputStream();
			targetChannel.connect();

			if (checkAck(din) != 0) {
				String error = "Error Reading input Stream";
				logger.error(error);
				throw new SSHException(error);
			}


			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			sout.write(buf, 0, 1);
			sout.flush();

			while (true) {
				int c = checkAck(sin);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				sin.read(buf, 0, 5);

				long fileSize = 0L;
				while (true) {
					if (sin.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ') break;
					fileSize = fileSize * 10L + (long) (buf[0] - '0');
				}

				String fileName = null;
				for (int i = 0; ; i++) {
					sin.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						fileName = new String(buf, 0, i);
						break;
					}
				}
				if (fileSize == 0L && !ignoreEmptyFile){
					String error = "Input file is empty...";
					logger.error(error);
					throw new JSchException(error);
				}
				String initData = "C0644 " + fileSize + " " + fileName + "\n";
				assert dout != null;
				dout.write(initData.getBytes());
				dout.flush();

				// send '\0' to source
				buf[0] = 0;
				sout.write(buf, 0, 1);
				sout.flush();

				int rLength;
				while (true) {
					if (buf.length < fileSize) rLength = buf.length;
					else rLength = (int) fileSize;
					rLength = sin.read(buf, 0, rLength); // read content of the source File
					if (rLength < 0) {
						// error
						break;
					}
					dout.write(buf, 0, rLength); // write to destination file
					fileSize -= rLength;
					if (fileSize == 0L) break;
				}

				// send '\0' to target
				buf[0] = 0;
				dout.write(buf, 0, 1);
				dout.flush();
				if (checkAck(din) != 0) {
					String error = "Error Reading input Stream";
					logger.error(error);
					throw new SSHException(error);
				}
				dout.close();
				dout = null;

				if (checkAck(sin) != 0) {
					String error = "Error transfering the file content";
					logger.error(error);
					throw new SSHException(error);
				}

				// send '\0'
				buf[0] = 0;
				sout.write(buf, 0, 1);
				sout.flush();
			}

		} catch (JSchException | IOException e) {
			logger.error(e.getMessage(), e);
			throw new SSHException(e.getMessage());
		} finally {
			if( null != sourceChannel && sourceChannel.isConnected()){
				sourceChannel.disconnect();
			}
			if( null != srcSession && srcSession.isConnected() ){
				logger.info("thirdPartyTransfer() -> Terminating session. From  : " + sourceFile + ", To : " + destinationFile);
				srcSession.disconnect();
			}

			if( null != targetChannel && targetChannel.isConnected()){
				targetChannel.disconnect();
			}
			if( null != session && session.isConnected() ){
				logger.info("thirdPartyTransfer() -> Terminating session. From  : " + sourceFile + ", To : " + destinationFile);
				session.disconnect();
			}
			try {
				if (dout != null) dout.close();
			} catch (IOException ee) {
				logger.error("", ee);
			}
			try {
				if (din != null) din.close();
			} catch (IOException ee) {
				logger.error("", ee);
			}
			try {
				if (sout != null) sout.close();
			} catch (IOException ee) {
				logger.error("", ee);
			}
			try {
				if (sin != null) sin.close();
			} catch (IOException ee) {
				logger.error("", ee);
			}
		}
		return destinationFile;
	}


	public String makeDirectory(String path) throws SSHException {

		logger.info("makeDirectory() -> Creating directory. Path : " + path);

		String command = "mkdir -p " + path;
		Channel channel = null;
		StandardOutReader stdOutReader = null;
		try {

			if (null != session && !session.isConnected()) {
				session.connect();
			}
			channel = session.openChannel("exec");
			stdOutReader = new StandardOutReader();

			((ChannelExec) channel).setCommand(command);


			((ChannelExec) channel).setErrStream(stdOutReader.getStandardError());

			channel.connect();

		} catch (JSchException e) {

			String err = "Unable to retrieve command output. Command - " + command +
					" on server - " + session.getHost() + ":" + session.getPort() +
					" connecting user name - "
					+ session.getUserName();
			logger.error(err, e);
			throw new SSHException(err);

		}finally {
			if( null != channel && channel.isConnected()){
				channel.disconnect();
			}
			if( null != session && session.isConnected() ){
				logger.info("makeDirectory() -> Terminating session. Create Directory : " + path);
				session.disconnect();
			}
			if( null != stdOutReader ){
				stdOutReader.onOutput(channel);
				if (stdOutReader.getStdErrorString().contains("mkdir:")) {
					throw new SSHException(stdOutReader.getStdErrorString());
				}
			}

		}
		return path;
	}
}
