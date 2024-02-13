package com.decouplink;

import static com.decouplink.Utilities.context;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

public class MemoryLeakTest {

    @Ignore
    public void testImplicitDispose() throws Exception {

        // create test object
        TestObject1 g = new TestObject1();
        TestObject2 input = new TestObject2(g);

        context(g).add(Object.class, input);

        // create queue and weak reference
        ReferenceQueue queue = new ReferenceQueue();

        WeakReference gRef = new WeakReference(g, queue);
        WeakReference iRef = new WeakReference(input, queue);

        // set hard reference to null
        // force garbage collection
        g = null;
        input = null;
        
        System.gc();
        Thread.sleep(1000);
        
        // Allocate memory until out-of-memory
        // garbage collector remove soft references
        List<byte[]> bytes = new ArrayList<>(10000);

        for (int i = 0; i < 100; i++) {
            bytes.add(new byte[10 * 1024 * 1024]); // allocate 10MB blocks
        }

        // References should not be enqueued due to value TestObject2 refering
        // to context object.
        assertTrue(gRef.isEnqueued());
        assertTrue(iRef.isEnqueued());
    }

    @Test
    public void testLinkDispose() throws InterruptedException {

        // create test object
        TestObject1 g = new TestObject1();
        TestObject2 input = new TestObject2(g);

        Link l = context(g).add(Object.class, input);

        // create queue and weak reference
        ReferenceQueue queue = new ReferenceQueue();

        WeakReference gRef = new WeakReference(g, queue);
        WeakReference iRef = new WeakReference(input, queue);

        // set hard reference to null
        g = null;
        input = null;
        l.dispose();
        l = null;

        // force garbage collection
        System.gc();
        Thread.sleep(1000);

        // soft reference should now be enqueued (no leak)
        assertTrue(gRef.isEnqueued());
        assertTrue(iRef.isEnqueued());
    }

    class TestObject1 {
    }

    class TestObject2 {

        private final Object obj;

        public TestObject2(TestObject1 obj) {
            this.obj = obj;
        }
    }
}
