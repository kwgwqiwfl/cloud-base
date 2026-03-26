package com.ring.welkin.common.queryapi;

import com.ring.welkin.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LombokBuilderTests {

    @Getter
    @AllArgsConstructor
    public static class Parent {
        private final String parentName;
        private final int parentAge;
    }

    @Getter
    public static class Child extends Parent {
        private final String childName;
        private final int childAge;

        @Builder
        public Child(String parentName, int parentAge, String childName, int childAge) {
            super(parentName, parentAge);
            this.childName = childName;
            this.childAge = childAge;
        }
    }

    @Test
    public void test1() {
        Child child = Child.builder().parentName("Andrea").parentAge(38).childName("Emma").childAge(6).build();
        assertEquals(child.getParentName(), "Andrea");
        assertEquals(child.getParentAge(), 38);
        assertEquals(child.getChildName(), "Emma");
        assertEquals(child.getChildAge(), 6);
    }

    @Getter
    public static class Child1 extends Parent {
        private final String childName;
        private final int childAge;

        @Builder(builderMethodName = "childBuilder")
        public Child1(String parentName, int parentAge, String childName, int childAge) {
            super(parentName, parentAge);
            this.childName = childName;
            this.childAge = childAge;
        }
    }

    @Test
    public void test2() {
        Child1 child = Child1.childBuilder().parentName("Andrea").parentAge(38).childName("Emma").childAge(6).build();
        assertEquals(child.getParentName(), "Andrea");
        assertEquals(child.getParentAge(), 38);
        assertEquals(child.getChildName(), "Emma");
        assertEquals(child.getChildAge(), 6);
    }

    @Getter
    public static class Student extends Child {

        private final String schoolName;

        @Builder(builderMethodName = "studentBuilder")
        public Student(String parentName, int parentAge, String childName, int childAge, String schoolName) {
            super(parentName, parentAge, childName, childAge);
            this.schoolName = schoolName;
        }
    }

    @Test
    public void test3() {
        Student student = Student.studentBuilder().parentName("Andrea").parentAge(38).childName("Emma").childAge(6).schoolName("Baeldung High School").build();

        assertEquals(student.getChildName(), "Emma");
        assertEquals(student.getChildAge(), 6);
        assertEquals(student.getParentName(), "Andrea");
        assertEquals(student.getParentAge(), 38);
        assertEquals(student.getSchoolName(), "Baeldung High School");
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class Animal {
        private String name;
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class Cat extends Animal {
        private String coatColor;
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class GarfieldCat extends Cat {
        private String cry;
    }

    @Test
    public void test4() {
        GarfieldCat cat = GarfieldCat.builder().name("加菲猫").coatColor("黑色").cry("喵喵").build();
        assertEquals(cat.getName(), "加菲猫");
        assertEquals(cat.getCoatColor(), "黑色");
        assertEquals(cat.getCry(), "喵喵");
        System.out.println(JsonUtils.toJson(cat));
    }

}
