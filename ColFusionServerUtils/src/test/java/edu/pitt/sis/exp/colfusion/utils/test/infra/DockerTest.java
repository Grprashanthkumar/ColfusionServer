package edu.pitt.sis.exp.colfusion.utils.test.infra;

import org.junit.Test;

import com.github.dockerjava.api.model.Info;

public class DockerTest extends DatabaseUnitTestBase {
	
	@Test
	public void testDocker1() {
		Info info = dockerClient.infoCmd().exec();
		System.out.print(info);
	}
	
	@Test
	public void testDocker2() {
		Info info = dockerClient.infoCmd().exec();
		System.out.print(info);
	}
	
	@Test
	public void testDocker3() {
		Info info = dockerClient.infoCmd().exec();
		System.out.print(info);
	}
	
	@Test
	public void testDocker4() {
		Info info = dockerClient.infoCmd().exec();
		System.out.print(info);
	}
	
	@Test
	public void testDocker5() {
		Info info = dockerClient.infoCmd().exec();
		System.out.print(info);
	}
}
