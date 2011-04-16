package test;

import org.infinispan.Cache;
import org.infinispan.context.Flag;

import java.util.Arrays;

/**
 * Sample application code.  For more examples visit http://community.jboss.org/wiki/5minutetutorialonInfinispan
 */
public class Application {

   public void doStuff(boolean observe) throws InterruptedException {

      Cache<String, String> c0 = SampleCacheContainer.getCache();

      System.out.printf("****** L1 enabled ? %s" , c0.getConfiguration().isL1CacheEnabled());

      Cache<String, String> c1 = SampleCacheContainer.getCache("one");
      Cache<String, String> c2 = SampleCacheContainer.getCache("two");
      Cache<String, String> c3 = SampleCacheContainer.getCache("three");

      System.out.printf("Local address is %s and membership is %s%n", c0.getCacheManager().getAddress(), c0.getCacheManager().getMembers());

      if (observe) {
         for (Cache<String, String> c: Arrays.asList(c0, c1, c2, c3)) System.out.printf("   Keys in cache %s are %s%n", c.getName(), c.keySet());
      } else {
         for (int i=0; i<100000; i++) {
            c0.getAdvancedCache().withFlags(Flag.FAIL_SILENTLY).put("k0_" + i, "v");
            c1.getAdvancedCache().withFlags(Flag.FAIL_SILENTLY).put("k1_" + i, "v");
            c2.getAdvancedCache().withFlags(Flag.FAIL_SILENTLY).put("k2_" + i, "v");
            c3.getAdvancedCache().withFlags(Flag.FAIL_SILENTLY).put("k3_" + i, "v");
            Thread.sleep(100);
            if (i % 10 == 0) System.out.printf("... put count %s%n", i);
         }
      }
   }

   public static void main(String[] args) throws Exception {
      boolean observe = false;
      if (args.length > 0 && args[0].equals("observe")) observe = true;
      System.out.println("\n\n\n   ********************************  \n\n\n");
      System.out.printf("Sample app in %s mode%n", observe ? "Observe" : "Run");
      Application a = new Application();
      a.doStuff(observe);
      System.out.println("Sample complete.");
      System.out.println("\n\n\n   ----- DONE -----  \n\n\n");
   }
}

