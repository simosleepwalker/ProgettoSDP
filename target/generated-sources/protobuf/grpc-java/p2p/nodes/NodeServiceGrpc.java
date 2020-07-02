package p2p.nodes;

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
    comments = "Source: node.proto")
public final class NodeServiceGrpc {

  private NodeServiceGrpc() {}

  public static final String SERVICE_NAME = "p2p.nodes.NodeService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<p2p.nodes.Node.NodeMessage,
      p2p.nodes.Node.OkMessage> METHOD_CHANGE_NEXT =
      io.grpc.MethodDescriptor.<p2p.nodes.Node.NodeMessage, p2p.nodes.Node.OkMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "p2p.nodes.NodeService", "changeNext"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              p2p.nodes.Node.NodeMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              p2p.nodes.Node.OkMessage.getDefaultInstance()))
          .setSchemaDescriptor(new NodeServiceMethodDescriptorSupplier("changeNext"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<p2p.nodes.Node.Token,
      p2p.nodes.Node.OkMessage> METHOD_RECV_TOKEN =
      io.grpc.MethodDescriptor.<p2p.nodes.Node.Token, p2p.nodes.Node.OkMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "p2p.nodes.NodeService", "recvToken"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              p2p.nodes.Node.Token.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              p2p.nodes.Node.OkMessage.getDefaultInstance()))
          .setSchemaDescriptor(new NodeServiceMethodDescriptorSupplier("recvToken"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeServiceStub newStub(io.grpc.Channel channel) {
    return new NodeServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NodeServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NodeServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class NodeServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void changeNext(p2p.nodes.Node.NodeMessage request,
        io.grpc.stub.StreamObserver<p2p.nodes.Node.OkMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CHANGE_NEXT, responseObserver);
    }

    /**
     */
    public void recvToken(p2p.nodes.Node.Token request,
        io.grpc.stub.StreamObserver<p2p.nodes.Node.OkMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RECV_TOKEN, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CHANGE_NEXT,
            asyncUnaryCall(
              new MethodHandlers<
                p2p.nodes.Node.NodeMessage,
                p2p.nodes.Node.OkMessage>(
                  this, METHODID_CHANGE_NEXT)))
          .addMethod(
            METHOD_RECV_TOKEN,
            asyncUnaryCall(
              new MethodHandlers<
                p2p.nodes.Node.Token,
                p2p.nodes.Node.OkMessage>(
                  this, METHODID_RECV_TOKEN)))
          .build();
    }
  }

  /**
   */
  public static final class NodeServiceStub extends io.grpc.stub.AbstractStub<NodeServiceStub> {
    private NodeServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeServiceStub(channel, callOptions);
    }

    /**
     */
    public void changeNext(p2p.nodes.Node.NodeMessage request,
        io.grpc.stub.StreamObserver<p2p.nodes.Node.OkMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CHANGE_NEXT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recvToken(p2p.nodes.Node.Token request,
        io.grpc.stub.StreamObserver<p2p.nodes.Node.OkMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RECV_TOKEN, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NodeServiceBlockingStub extends io.grpc.stub.AbstractStub<NodeServiceBlockingStub> {
    private NodeServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public p2p.nodes.Node.OkMessage changeNext(p2p.nodes.Node.NodeMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CHANGE_NEXT, getCallOptions(), request);
    }

    /**
     */
    public p2p.nodes.Node.OkMessage recvToken(p2p.nodes.Node.Token request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RECV_TOKEN, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NodeServiceFutureStub extends io.grpc.stub.AbstractStub<NodeServiceFutureStub> {
    private NodeServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<p2p.nodes.Node.OkMessage> changeNext(
        p2p.nodes.Node.NodeMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CHANGE_NEXT, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<p2p.nodes.Node.OkMessage> recvToken(
        p2p.nodes.Node.Token request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RECV_TOKEN, getCallOptions()), request);
    }
  }

  private static final int METHODID_CHANGE_NEXT = 0;
  private static final int METHODID_RECV_TOKEN = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NodeServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NodeServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHANGE_NEXT:
          serviceImpl.changeNext((p2p.nodes.Node.NodeMessage) request,
              (io.grpc.stub.StreamObserver<p2p.nodes.Node.OkMessage>) responseObserver);
          break;
        case METHODID_RECV_TOKEN:
          serviceImpl.recvToken((p2p.nodes.Node.Token) request,
              (io.grpc.stub.StreamObserver<p2p.nodes.Node.OkMessage>) responseObserver);
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

  private static abstract class NodeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return p2p.nodes.Node.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NodeService");
    }
  }

  private static final class NodeServiceFileDescriptorSupplier
      extends NodeServiceBaseDescriptorSupplier {
    NodeServiceFileDescriptorSupplier() {}
  }

  private static final class NodeServiceMethodDescriptorSupplier
      extends NodeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NodeServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (NodeServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeServiceFileDescriptorSupplier())
              .addMethod(METHOD_CHANGE_NEXT)
              .addMethod(METHOD_RECV_TOKEN)
              .build();
        }
      }
    }
    return result;
  }
}
