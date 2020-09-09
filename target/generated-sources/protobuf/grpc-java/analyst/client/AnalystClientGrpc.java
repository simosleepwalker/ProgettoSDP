package analyst.client;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: analyst.proto")
public final class AnalystClientGrpc {

  private AnalystClientGrpc() {}

  public static final String SERVICE_NAME = "analyst.client.AnalystClient";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<analyst.client.Analyst.notificationMessage,
      analyst.client.Analyst.okMessage> METHOD_NOTIFY =
      io.grpc.MethodDescriptor.<analyst.client.Analyst.notificationMessage, analyst.client.Analyst.okMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "analyst.client.AnalystClient", "notify"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              analyst.client.Analyst.notificationMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              analyst.client.Analyst.okMessage.getDefaultInstance()))
          .setSchemaDescriptor(new AnalystClientMethodDescriptorSupplier("notify"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AnalystClientStub newStub(io.grpc.Channel channel) {
    return new AnalystClientStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AnalystClientBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AnalystClientBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AnalystClientFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AnalystClientFutureStub(channel);
  }

  /**
   */
  public static abstract class AnalystClientImplBase implements io.grpc.BindableService {

    /**
     */
    public void notify(analyst.client.Analyst.notificationMessage request,
        io.grpc.stub.StreamObserver<analyst.client.Analyst.okMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_NOTIFY, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_NOTIFY,
            asyncUnaryCall(
              new MethodHandlers<
                analyst.client.Analyst.notificationMessage,
                analyst.client.Analyst.okMessage>(
                  this, METHODID_NOTIFY)))
          .build();
    }
  }

  /**
   */
  public static final class AnalystClientStub extends io.grpc.stub.AbstractStub<AnalystClientStub> {
    private AnalystClientStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AnalystClientStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AnalystClientStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AnalystClientStub(channel, callOptions);
    }

    /**
     */
    public void notify(analyst.client.Analyst.notificationMessage request,
        io.grpc.stub.StreamObserver<analyst.client.Analyst.okMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_NOTIFY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AnalystClientBlockingStub extends io.grpc.stub.AbstractStub<AnalystClientBlockingStub> {
    private AnalystClientBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AnalystClientBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AnalystClientBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AnalystClientBlockingStub(channel, callOptions);
    }

    /**
     */
    public analyst.client.Analyst.okMessage notify(analyst.client.Analyst.notificationMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_NOTIFY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AnalystClientFutureStub extends io.grpc.stub.AbstractStub<AnalystClientFutureStub> {
    private AnalystClientFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AnalystClientFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AnalystClientFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AnalystClientFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<analyst.client.Analyst.okMessage> notify(
        analyst.client.Analyst.notificationMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_NOTIFY, getCallOptions()), request);
    }
  }

  private static final int METHODID_NOTIFY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AnalystClientImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AnalystClientImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NOTIFY:
          serviceImpl.notify((analyst.client.Analyst.notificationMessage) request,
              (io.grpc.stub.StreamObserver<analyst.client.Analyst.okMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AnalystClientBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AnalystClientBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return analyst.client.Analyst.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AnalystClient");
    }
  }

  private static final class AnalystClientFileDescriptorSupplier
      extends AnalystClientBaseDescriptorSupplier {
    AnalystClientFileDescriptorSupplier() {}
  }

  private static final class AnalystClientMethodDescriptorSupplier
      extends AnalystClientBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AnalystClientMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AnalystClientGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AnalystClientFileDescriptorSupplier())
              .addMethod(METHOD_NOTIFY)
              .build();
        }
      }
    }
    return result;
  }
}
