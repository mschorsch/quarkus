package io.quarkus.virtual.graphql;

import java.util.concurrent.CompletionStage;

import jakarta.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;

/**
 * Testing the thread used.
 * Copied from graphql extension to make sure threading without @RunOnVirtualThread runs the same.
 */
@QuarkusTest
public class GraphQLThreadTest extends AbstractGraphQLTest {

    @Test
    public void testOnlyObject() {

        String fooRequest = getPayload("{\n" +
                "  onlyObject {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.onlyObject.name", Matchers.startsWith("executor-thread"))
                .and()
                .body("data.onlyObject.vertxContextClassName", Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));
    }

    @Test
    public void testAnnotatedNonBlockingObject() {

        String fooRequest = getPayload("{\n" +
                "  annotatedNonBlockingObject {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.annotatedNonBlockingObject.name", Matchers.startsWith("vert.x-eventloop-thread"))
                .and()
                .body("data.annotatedNonBlockingObject.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));
    }

    @Test
    public void testAnnotatedBlockingObject() {

        String fooRequest = getPayload("{\n" +
                "  annotatedBlockingObject {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.annotatedBlockingObject.name", Matchers.startsWith("executor-thread"))
                .and()
                .body("data.annotatedBlockingObject.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));
    }

    @Test
    public void testOnlyReactiveUni() {

        String fooRequest = getPayload("{\n" +
                "  onlyReactiveUni {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.onlyReactiveUni.name", Matchers.startsWith("vert.x-eventloop-thread"))
                .and()
                .body("data.onlyReactiveUni.vertxContextClassName", Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));
    }

    @Test
    public void testOnlyReactiveUniWithDelay() {

        String fooRequest = getPayload("{\n" +
                "  onlyReactiveUniWithDelay {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.onlyReactiveUniWithDelay.name", Matchers.startsWith("vert.x-eventloop-thread"))
                .and()
                .body("data.onlyReactiveUniWithDelay.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));
    }

    @Test
    public void testAnnotatedBlockingReactiveUni() {

        String fooRequest = getPayload("{\n" +
                "  annotatedBlockingReactiveUni {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.annotatedBlockingReactiveUni.name", Matchers.startsWith("executor-thread"))
                .and()
                .body("data.annotatedBlockingReactiveUni.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));

    }

    @Test
    public void testAnnotatedNonBlockingReactiveUni() {

        String fooRequest = getPayload("{\n" +
                "  annotatedNonBlockingReactiveUni {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.annotatedNonBlockingReactiveUni.name", Matchers.startsWith("vert.x-eventloop-thread"))
                .and()
                .body("data.annotatedNonBlockingReactiveUni.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));

    }

    @Test
    public void testOnlyCompletionStage() {

        String fooRequest = getPayload("{\n" +
                "  onlyCompletionStage {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.onlyCompletionStage.name", Matchers.startsWith("vert.x-eventloop-thread"))
                .and()
                .body("data.onlyCompletionStage.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));

    }

    @Test
    public void testAnnotatedBlockingCompletionStage() {

        String fooRequest = getPayload("{\n" +
                "  annotatedBlockingCompletionStage {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.annotatedBlockingCompletionStage.name", Matchers.startsWith("executor-thread"))
                .and()
                .body("data.annotatedBlockingCompletionStage.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));

    }

    @Test
    public void testAnnotatedNonBlockingCompletionStage() {

        String fooRequest = getPayload("{\n" +
                "  annotatedNonBlockingCompletionStage {\n" +
                "    name\n" +
                "    priority\n" +
                "    state\n" +
                "    group\n" +
                "    vertxContextClassName\n" +
                "  }\n" +
                "}");

        RestAssured.given().when()
                .accept(MEDIATYPE_JSON)
                .contentType(MEDIATYPE_JSON)
                .body(fooRequest)
                .post("/graphql")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .log().body().and()
                .body("data.annotatedNonBlockingCompletionStage.name", Matchers.startsWith("vert.x-eventloop-thread"))
                .and()
                .body("data.annotatedNonBlockingCompletionStage.vertxContextClassName",
                        Matchers.equalTo("io.vertx.core.impl.DuplicatedContext"));

    }

    @GraphQLApi
    public static class TestThreadResource {

        @Inject
        Vertx vertx;

        // Return type Object
        @Query
        public TestThread onlyObject() {
            return getTestThread();
        }

        // Return type Object, Annotated with @NonBlocking
        @Query
        @NonBlocking
        public TestThread annotatedNonBlockingObject() {
            return getTestThread();
        }

        // Return type Object with @Blocking (default)
        @Query
        @Blocking
        public TestThread annotatedBlockingObject() {
            return getTestThread();
        }

        // Return type Uni
        @Query
        public Uni<TestThread> onlyReactiveUni() {
            return Uni.createFrom().item(() -> getTestThread());
        }

        // Return type Uni With Delay
        @Query
        public Uni<TestThread> onlyReactiveUniWithDelay() {
            return Uni.createFrom().emitter(
                    emitter -> {
                        vertx.setTimer(1000, x -> emitter.complete(getTestThread()));
                    });
        }

        // Return type Reactive with @Blocking
        @Query
        @Blocking
        public Uni<TestThread> annotatedBlockingReactiveUni() {
            return Uni.createFrom().item(() -> getTestThread());
        }

        // Return type Reactive with @NonBlocking (default)
        @Query
        @NonBlocking
        public Uni<TestThread> annotatedNonBlockingReactiveUni() {
            return Uni.createFrom().item(() -> getTestThread());
        }

        @Query
        public CompletionStage<TestThread> onlyCompletionStage() {
            return Uni.createFrom().item(() -> getTestThread()).subscribeAsCompletionStage();
        }

        // Return type CompletionStage with @Blocking
        @Query
        @Blocking
        public CompletionStage<TestThread> annotatedBlockingCompletionStage() {
            return Uni.createFrom().item(() -> getTestThread()).subscribeAsCompletionStage();
        }

        // Return type CompletionStage with @NonBlocking (default)
        @Query
        @NonBlocking
        public CompletionStage<TestThread> annotatedNonBlockingCompletionStage() {
            return Uni.createFrom().item(() -> getTestThread()).subscribeAsCompletionStage();
        }

        private TestThread getTestThread() {
            Thread t = Thread.currentThread();
            long id = t.getId();
            String name = t.getName();
            int priority = t.getPriority();
            String state = t.getState().name();
            String group = t.getThreadGroup().getName();
            return new TestThread(id, name, priority, state, group);
        }
    }
}
