package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.xom.ctnr.Ref;
import com.github.nicholasmoser.xom.ctnr.Value;
import com.github.nicholasmoser.xom.ctnr.XCollection;
import com.github.nicholasmoser.xom.ctnr.XContainer;

import java.util.List;

public class XomModify {
    /**
     * Remove an XContainer from the xom and update all reference numbers to account for the removal.
     *
     * @param container The container to remove.
     * @param xom The xom to remove it from.
     */
    public static void removeXContainer(XContainer container, Xom xom) {
        List<XContainer> containers = xom.containers();
        int index = containers.indexOf(container);
        if (index == -1) {
            throw new IllegalArgumentException("Unable to find provided container " + container.name());
        }
        removeXContainer(index, xom);
    }

    /**
     * Remove an XContainer from the xom and update all reference numbers to account for the removal.
     *
     * @param index The index of the type to remove.
     * @param xom The xom to remove it from.
     */
    public static void removeXContainer(int index, Xom xom) {
        List<XContainer> containers = xom.containers();
        containers.remove(index);
        xom.header().decrementContainerCount();
        // Update reference values to point to adjusted values, basically subtract 1 if after the removed container
        for (XContainer current : containers) {
            for (Value value : current.values()) {
                if (value instanceof Ref ref) {
                    // Reference values start at 1, so subtract 1 for 0 based indices
                    updateReference(ref, index);
                } else if (value instanceof XCollection collection) {
                    for (Value child : collection.values()) {
                        if (child instanceof Ref ref) {
                            updateReference(ref, index);
                        }
                    }
                }
            }
        }
    }

    /**
     * Remove a type from the list of Xom types.
     *
     * @param i The index of the type to remove.
     * @param xom The xom to remove the type from.
     */
    public static void removeXType(int i, Xom xom) {
        xom.types().remove(i);
        xom.header().decrementNumberOfTypes();
    }

    private static void updateReference(Ref ref, int index) {
        // References start at 1, so subtract 1 to compare to the 0-based provided index
        int refVal = ref.value() - 1;
        if (refVal == index){
            // This reference references the removed container, so change it to 0
            ref.setValue(0);
        } else if (refVal > index) {
            // This reference is larger than the removed index, so decrement it by 1
            ref.setValue(ref.value() - 1);
        }
    }

    public static void printReferences(Xom xom) {
        List<XContainer> containers = xom.containers();
        for (XContainer current : containers) {
            for (Value value : current.values()) {
                if (value instanceof Ref ref) {
                    System.out.printf("%s->%s %d\n", current.name(), value.name(), ref.value());
                } else if (value instanceof XCollection collection) {
                    for (Value child : collection.values()) {
                        if (child instanceof Ref ref) {
                            System.out.printf("%s->%s->%s %d\n", current.name(), value.name(), child.name(), ref.value());
                        }
                    }
                }
            }
        }
    }
}
