/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.services.elasticfilesystem;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import com.amazonaws.services.elasticfilesystem.model.*;

/**
 * Asynchronous client for accessing AmazonElasticFileSystem.
 * All asynchronous calls made using this client are non-blocking. Callers could either
 * process the result and handle the exceptions in the worker thread by providing a callback handler
 * when making the call, or use the returned Future object to check the result of the call in the calling thread.
 * Amazon Elastic File System
 */
public class AmazonElasticFileSystemAsyncClient extends AmazonElasticFileSystemClient
        implements AmazonElasticFileSystemAsync {

    /**
     * Executor service for executing asynchronous requests.
     */
    private ExecutorService executorService;

    private static final int DEFAULT_THREAD_POOL_SIZE = 50;

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem.  A credentials provider chain will be used
     * that searches for credentials in this order:
     * <ul>
     *  <li> Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY </li>
     *  <li> Java System Properties - aws.accessKeyId and aws.secretKey </li>
     *  <li> Instance profile credentials delivered through the Amazon EC2 metadata service </li>
     * </ul>
     *
     * <p>
     * All service calls made using this new client object are blocking, and will not
     * return until the service call completes.
     *
     * @see DefaultAWSCredentialsProviderChain
     */
    public AmazonElasticFileSystemAsyncClient() {
        this(new DefaultAWSCredentialsProviderChain());
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem.  A credentials provider chain will be used
     * that searches for credentials in this order:
     * <ul>
     *  <li> Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY </li>
     *  <li> Java System Properties - aws.accessKeyId and aws.secretKey </li>
     *  <li> Instance profile credentials delivered through the Amazon EC2 metadata service </li>
     * </ul>
     *
     * <p>
     * All service calls made using this new client object are blocking, and will not
     * return until the service call completes.
     *
     * @param clientConfiguration The client configuration options controlling how this
     *                       client connects to AmazonElasticFileSystem
     *                       (ex: proxy settings, retry counts, etc.).
     *
     * @see DefaultAWSCredentialsProviderChain
     */
    public AmazonElasticFileSystemAsyncClient(ClientConfiguration clientConfiguration) {
        this(new DefaultAWSCredentialsProviderChain(), clientConfiguration, Executors.newFixedThreadPool(clientConfiguration.getMaxConnections()));
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials.
     * Default client settings will be used, and a fixed size thread pool will be
     * created for executing the asynchronous tasks.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentials The AWS credentials (access key ID and secret key) to use
     *                       when authenticating with AWS services.
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentials awsCredentials) {
        this(awsCredentials, Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE));
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials
     * and executor service.  Default client settings will be used.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentials
     *            The AWS credentials (access key ID and secret key) to use
     *            when authenticating with AWS services.
     * @param executorService
     *            The executor service by which all asynchronous requests will
     *            be executed.
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentials awsCredentials, ExecutorService executorService) {
        super(awsCredentials);
        this.executorService = executorService;
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials,
     * executor service, and client configuration options.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentials
     *            The AWS credentials (access key ID and secret key) to use
     *            when authenticating with AWS services.
     * @param clientConfiguration
     *            Client configuration options (ex: max retry limit, proxy
     *            settings, etc).
     * @param executorService
     *            The executor service by which all asynchronous requests will
     *            be executed.
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentials awsCredentials,
                ClientConfiguration clientConfiguration, ExecutorService executorService) {
        super(awsCredentials, clientConfiguration);
        this.executorService = executorService;
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials provider.
     * Default client settings will be used, and a fixed size thread pool will be
     * created for executing the asynchronous tasks.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentialsProvider
     *            The AWS credentials provider which will provide credentials
     *            to authenticate requests with AWS services.
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentialsProvider awsCredentialsProvider) {
        this(awsCredentialsProvider, Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE));
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials provider
     * and executor service.  Default client settings will be used.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentialsProvider
     *            The AWS credentials provider which will provide credentials
     *            to authenticate requests with AWS services.
     * @param executorService
     *            The executor service by which all asynchronous requests will
     *            be executed.
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentialsProvider awsCredentialsProvider, ExecutorService executorService) {
        this(awsCredentialsProvider, new ClientConfiguration(), executorService);
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials
     * provider and client configuration options.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentialsProvider
     *            The AWS credentials provider which will provide credentials
     *            to authenticate requests with AWS services.
     * @param clientConfiguration
     *            Client configuration options (ex: max retry limit, proxy
     *            settings, etc).
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentialsProvider awsCredentialsProvider,
                ClientConfiguration clientConfiguration) {
        this(awsCredentialsProvider, clientConfiguration, Executors.newFixedThreadPool(clientConfiguration.getMaxConnections()));
    }

    /**
     * Constructs a new asynchronous client to invoke service methods on
     * AmazonElasticFileSystem using the specified AWS account credentials
     * provider, executor service, and client configuration options.
     *
     * <p>
     * All calls made using this new client object are non-blocking, and will immediately
     * return a Java Future object that the caller can later check to see if the service
     * call has actually completed.
     *
     * @param awsCredentialsProvider
     *            The AWS credentials provider which will provide credentials
     *            to authenticate requests with AWS services.
     * @param clientConfiguration
     *            Client configuration options (ex: max retry limit, proxy
     *            settings, etc).
     * @param executorService
     *            The executor service by which all asynchronous requests will
     *            be executed.
     */
    public AmazonElasticFileSystemAsyncClient(AWSCredentialsProvider awsCredentialsProvider,
                ClientConfiguration clientConfiguration, ExecutorService executorService) {
        super(awsCredentialsProvider, clientConfiguration);
        this.executorService = executorService;
    }

    /**
     * Returns the executor service used by this async client to execute
     * requests.
     *
     * @return The executor service used by this async client to execute
     *         requests.
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Shuts down the client, releasing all managed resources. This includes
     * forcibly terminating all pending asynchronous service calls. Clients who
     * wish to give pending asynchronous service calls time to complete should
     * call getExecutorService().shutdown() followed by
     * getExecutorService().awaitTermination() prior to calling this method.
     */
    @Override
    public void shutdown() {
        super.shutdown();
        executorService.shutdownNow();
    }
            
    /**
     * <p>
     * Creates a new, empty file system. The operation requires a creation
     * token in the request that Amazon EFS uses to ensure idempotent
     * creation (calling the operation with same creation token has no
     * effect). If a file system does not currently exist that is owned by
     * the caller's AWS account with the specified creation token, this
     * operation does the following:
     * </p>
     * 
     * <ul>
     * <li>Creates a new, empty file system. The file system will have an
     * Amazon EFS assigned ID, and an initial lifecycle state "creating".
     * </li>
     * <li> Returns with the description of the created file system. </li>
     * 
     * </ul>
     * <p>
     * Otherwise, this operation returns a
     * <code>FileSystemAlreadyExists</code> error with the ID of the existing
     * file system.
     * </p>
     * <p>
     * <b>NOTE:</b>For basic use cases, you can use a randomly generated
     * UUID for the creation token.
     * </p>
     * <p>
     * The idempotent operation allows you to retry a
     * <code>CreateFileSystem</code> call without risk of creating an extra
     * file system. This can happen when an initial call fails in a way that
     * leaves it uncertain whether or not a file system was actually created.
     * An example might be that a transport level timeout occurred or your
     * connection was reset. As long as you use the same creation token, if
     * the initial call had succeeded in creating a file system, the client
     * can learn of its existence from the
     * <code>FileSystemAlreadyExists</code> error.
     * </p>
     * <p>
     * <b>NOTE:</b>The CreateFileSystem call returns while the file system's
     * lifecycle state is still "creating". You can check the file system
     * creation status by calling the DescribeFileSystems API, which among
     * other things returns the file system state.
     * </p>
     * <p>
     * After the file system is fully created, Amazon EFS sets its lifecycle
     * state to "available", at which point you can create one or more mount
     * targets for the file system (CreateMountTarget) in your VPC. You mount
     * your Amazon EFS file system on an EC2 instances in your VPC via the
     * mount target. For more information, see
     * <a href="http://docs.aws.amazon.com/efs/latest/ug/how-it-works.html"> Amazon EFS: How it Works </a>
     * 
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:CreateFileSystem</code> action.
     * </p>
     *
     * @param createFileSystemRequest Container for the necessary parameters
     *           to execute the CreateFileSystem operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         CreateFileSystem service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<CreateFileSystemResult> createFileSystemAsync(final CreateFileSystemRequest createFileSystemRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<CreateFileSystemResult>() {
            public CreateFileSystemResult call() throws Exception {
                return createFileSystem(createFileSystemRequest);
        }
    });
    }

    /**
     * <p>
     * Creates a new, empty file system. The operation requires a creation
     * token in the request that Amazon EFS uses to ensure idempotent
     * creation (calling the operation with same creation token has no
     * effect). If a file system does not currently exist that is owned by
     * the caller's AWS account with the specified creation token, this
     * operation does the following:
     * </p>
     * 
     * <ul>
     * <li>Creates a new, empty file system. The file system will have an
     * Amazon EFS assigned ID, and an initial lifecycle state "creating".
     * </li>
     * <li> Returns with the description of the created file system. </li>
     * 
     * </ul>
     * <p>
     * Otherwise, this operation returns a
     * <code>FileSystemAlreadyExists</code> error with the ID of the existing
     * file system.
     * </p>
     * <p>
     * <b>NOTE:</b>For basic use cases, you can use a randomly generated
     * UUID for the creation token.
     * </p>
     * <p>
     * The idempotent operation allows you to retry a
     * <code>CreateFileSystem</code> call without risk of creating an extra
     * file system. This can happen when an initial call fails in a way that
     * leaves it uncertain whether or not a file system was actually created.
     * An example might be that a transport level timeout occurred or your
     * connection was reset. As long as you use the same creation token, if
     * the initial call had succeeded in creating a file system, the client
     * can learn of its existence from the
     * <code>FileSystemAlreadyExists</code> error.
     * </p>
     * <p>
     * <b>NOTE:</b>The CreateFileSystem call returns while the file system's
     * lifecycle state is still "creating". You can check the file system
     * creation status by calling the DescribeFileSystems API, which among
     * other things returns the file system state.
     * </p>
     * <p>
     * After the file system is fully created, Amazon EFS sets its lifecycle
     * state to "available", at which point you can create one or more mount
     * targets for the file system (CreateMountTarget) in your VPC. You mount
     * your Amazon EFS file system on an EC2 instances in your VPC via the
     * mount target. For more information, see
     * <a href="http://docs.aws.amazon.com/efs/latest/ug/how-it-works.html"> Amazon EFS: How it Works </a>
     * 
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:CreateFileSystem</code> action.
     * </p>
     *
     * @param createFileSystemRequest Container for the necessary parameters
     *           to execute the CreateFileSystem operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         CreateFileSystem service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<CreateFileSystemResult> createFileSystemAsync(
            final CreateFileSystemRequest createFileSystemRequest,
            final AsyncHandler<CreateFileSystemRequest, CreateFileSystemResult> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<CreateFileSystemResult>() {
            public CreateFileSystemResult call() throws Exception {
              CreateFileSystemResult result;
                try {
                result = createFileSystem(createFileSystemRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(createFileSystemRequest, result);
                 return result;
        }
    });
    }
    
    /**
     * <p>
     * Deletes the specified mount target.
     * </p>
     * <p>
     * This operation forcibly breaks any mounts of the file system via the
     * mount target being deleted, which might disrupt instances or
     * applications using those mounts. To avoid applications getting cut off
     * abruptly, you might consider unmounting any mounts of the mount
     * target, if feasible. The operation also deletes the associated network
     * interface. Uncommitted writes may be lost, but breaking a mount target
     * using this operation does not corrupt the file system itself. The file
     * system you created remains. You can mount an EC2 instance in your VPC
     * using another mount target.
     * </p>
     * <p>
     * This operation requires permission for the following action on the
     * file system:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:DeleteMountTarget</code> </li>
     * 
     * </ul>
     * <p>
     * <b>NOTE:</b>The DeleteMountTarget call returns while the mount target
     * state is still "deleting". You can check the mount target deletion by
     * calling the DescribeMountTargets API, which returns a list of mount
     * target descriptions for the given file system.
     * </p>
     * <p>
     * The operation also requires permission for the following Amazon EC2
     * action on the mount target's network interface:
     * </p>
     * 
     * <ul>
     * <li> <code>ec2:DeleteNetworkInterface</code> </li>
     * 
     * </ul>
     *
     * @param deleteMountTargetRequest Container for the necessary parameters
     *           to execute the DeleteMountTarget operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DeleteMountTarget service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> deleteMountTargetAsync(final DeleteMountTargetRequest deleteMountTargetRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
                deleteMountTarget(deleteMountTargetRequest);
                return null;
        }
    });
    }

    /**
     * <p>
     * Deletes the specified mount target.
     * </p>
     * <p>
     * This operation forcibly breaks any mounts of the file system via the
     * mount target being deleted, which might disrupt instances or
     * applications using those mounts. To avoid applications getting cut off
     * abruptly, you might consider unmounting any mounts of the mount
     * target, if feasible. The operation also deletes the associated network
     * interface. Uncommitted writes may be lost, but breaking a mount target
     * using this operation does not corrupt the file system itself. The file
     * system you created remains. You can mount an EC2 instance in your VPC
     * using another mount target.
     * </p>
     * <p>
     * This operation requires permission for the following action on the
     * file system:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:DeleteMountTarget</code> </li>
     * 
     * </ul>
     * <p>
     * <b>NOTE:</b>The DeleteMountTarget call returns while the mount target
     * state is still "deleting". You can check the mount target deletion by
     * calling the DescribeMountTargets API, which returns a list of mount
     * target descriptions for the given file system.
     * </p>
     * <p>
     * The operation also requires permission for the following Amazon EC2
     * action on the mount target's network interface:
     * </p>
     * 
     * <ul>
     * <li> <code>ec2:DeleteNetworkInterface</code> </li>
     * 
     * </ul>
     *
     * @param deleteMountTargetRequest Container for the necessary parameters
     *           to execute the DeleteMountTarget operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DeleteMountTarget service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> deleteMountTargetAsync(
            final DeleteMountTargetRequest deleteMountTargetRequest,
            final AsyncHandler<DeleteMountTargetRequest, Void> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
              try {
                deleteMountTarget(deleteMountTargetRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(deleteMountTargetRequest, null);
                 return null;
        }
    });
    }
    
    /**
     * <p>
     * Returns the description of a specific Amazon EFS file system if
     * either the file system <code>CreationToken</code> or the
     * <code>FileSystemId</code> is provided; otherwise, returns descriptions
     * of all file systems owned by the caller's AWS account in the AWS
     * region of the endpoint that you're calling.
     * </p>
     * <p>
     * When retrieving all file system descriptions, you can optionally
     * specify the <code>MaxItems</code> parameter to limit the number of
     * descriptions in a response. If more file system descriptions remain,
     * Amazon EFS returns a <code>NextMarker</code> , an opaque token, in the
     * response. In this case, you should send a subsequent request with the
     * <code>Marker</code> request parameter set to the value of
     * <code>NextMarker</code> .
     * </p>
     * <p>
     * So to retrieve a list of your file system descriptions, the expected
     * usage of this API is an iterative process of first calling
     * <code>DescribeFileSystems</code> without the <code>Marker</code> and
     * then continuing to call it with the <code>Marker</code> parameter set
     * to the value of the <code>NextMarker</code> from the previous response
     * until the response has no <code>NextMarker</code> .
     * </p>
     * <p>
     * Note that the implementation may return fewer than
     * <code>MaxItems</code> file system descriptions while still including a
     * <code>NextMarker</code> value.
     * </p>
     * <p>
     * The order of file systems returned in the response of one
     * <code>DescribeFileSystems</code> call, and the order of file systems
     * returned across the responses of a multi-call iteration, is
     * unspecified.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DescribeFileSystems</code> action.
     * </p>
     *
     * @param describeFileSystemsRequest Container for the necessary
     *           parameters to execute the DescribeFileSystems operation on
     *           AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeFileSystems service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeFileSystemsResult> describeFileSystemsAsync(final DescribeFileSystemsRequest describeFileSystemsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeFileSystemsResult>() {
            public DescribeFileSystemsResult call() throws Exception {
                return describeFileSystems(describeFileSystemsRequest);
        }
    });
    }

    /**
     * <p>
     * Returns the description of a specific Amazon EFS file system if
     * either the file system <code>CreationToken</code> or the
     * <code>FileSystemId</code> is provided; otherwise, returns descriptions
     * of all file systems owned by the caller's AWS account in the AWS
     * region of the endpoint that you're calling.
     * </p>
     * <p>
     * When retrieving all file system descriptions, you can optionally
     * specify the <code>MaxItems</code> parameter to limit the number of
     * descriptions in a response. If more file system descriptions remain,
     * Amazon EFS returns a <code>NextMarker</code> , an opaque token, in the
     * response. In this case, you should send a subsequent request with the
     * <code>Marker</code> request parameter set to the value of
     * <code>NextMarker</code> .
     * </p>
     * <p>
     * So to retrieve a list of your file system descriptions, the expected
     * usage of this API is an iterative process of first calling
     * <code>DescribeFileSystems</code> without the <code>Marker</code> and
     * then continuing to call it with the <code>Marker</code> parameter set
     * to the value of the <code>NextMarker</code> from the previous response
     * until the response has no <code>NextMarker</code> .
     * </p>
     * <p>
     * Note that the implementation may return fewer than
     * <code>MaxItems</code> file system descriptions while still including a
     * <code>NextMarker</code> value.
     * </p>
     * <p>
     * The order of file systems returned in the response of one
     * <code>DescribeFileSystems</code> call, and the order of file systems
     * returned across the responses of a multi-call iteration, is
     * unspecified.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DescribeFileSystems</code> action.
     * </p>
     *
     * @param describeFileSystemsRequest Container for the necessary
     *           parameters to execute the DescribeFileSystems operation on
     *           AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeFileSystems service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeFileSystemsResult> describeFileSystemsAsync(
            final DescribeFileSystemsRequest describeFileSystemsRequest,
            final AsyncHandler<DescribeFileSystemsRequest, DescribeFileSystemsResult> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeFileSystemsResult>() {
            public DescribeFileSystemsResult call() throws Exception {
              DescribeFileSystemsResult result;
                try {
                result = describeFileSystems(describeFileSystemsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(describeFileSystemsRequest, result);
                 return result;
        }
    });
    }
    
    /**
     * <p>
     * Creates or overwrites tags associated with a file system. Each tag is
     * a key-value pair. If a tag key specified in the request already exists
     * on the file system, this operation overwrites its value with the value
     * provided in the request. If you add the "Name" tag to your file
     * system, Amazon EFS returns it in the response to the
     * DescribeFileSystems API.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:CreateTags</code> action.
     * </p>
     *
     * @param createTagsRequest Container for the necessary parameters to
     *           execute the CreateTags operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         CreateTags service method, as returned by AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> createTagsAsync(final CreateTagsRequest createTagsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
                createTags(createTagsRequest);
                return null;
        }
    });
    }

    /**
     * <p>
     * Creates or overwrites tags associated with a file system. Each tag is
     * a key-value pair. If a tag key specified in the request already exists
     * on the file system, this operation overwrites its value with the value
     * provided in the request. If you add the "Name" tag to your file
     * system, Amazon EFS returns it in the response to the
     * DescribeFileSystems API.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:CreateTags</code> action.
     * </p>
     *
     * @param createTagsRequest Container for the necessary parameters to
     *           execute the CreateTags operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         CreateTags service method, as returned by AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> createTagsAsync(
            final CreateTagsRequest createTagsRequest,
            final AsyncHandler<CreateTagsRequest, Void> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
              try {
                createTags(createTagsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(createTagsRequest, null);
                 return null;
        }
    });
    }
    
    /**
     * <p>
     * Deletes a file system, permanently severing access to its contents.
     * Upon return, the file system no longer exists and you will not be able
     * to access any contents of the deleted file system.
     * </p>
     * <p>
     * You cannot delete a file system that is in use. That is, if the file
     * system has any mount targets, you must first delete them. For more
     * information, see DescribeMountTargets and DeleteMountTarget.
     * </p>
     * <p>
     * <b>NOTE:</b>The DeleteFileSystem call returns while the file system
     * state is still "deleting". You can check the file system deletion
     * status by calling the DescribeFileSystems API, which returns a list of
     * file systems in your account. If you pass file system ID or creation
     * token for the deleted file system, the DescribeFileSystems will return
     * a 404 "FileSystemNotFound" error.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DeleteFileSystem</code> action.
     * </p>
     *
     * @param deleteFileSystemRequest Container for the necessary parameters
     *           to execute the DeleteFileSystem operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DeleteFileSystem service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> deleteFileSystemAsync(final DeleteFileSystemRequest deleteFileSystemRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
                deleteFileSystem(deleteFileSystemRequest);
                return null;
        }
    });
    }

    /**
     * <p>
     * Deletes a file system, permanently severing access to its contents.
     * Upon return, the file system no longer exists and you will not be able
     * to access any contents of the deleted file system.
     * </p>
     * <p>
     * You cannot delete a file system that is in use. That is, if the file
     * system has any mount targets, you must first delete them. For more
     * information, see DescribeMountTargets and DeleteMountTarget.
     * </p>
     * <p>
     * <b>NOTE:</b>The DeleteFileSystem call returns while the file system
     * state is still "deleting". You can check the file system deletion
     * status by calling the DescribeFileSystems API, which returns a list of
     * file systems in your account. If you pass file system ID or creation
     * token for the deleted file system, the DescribeFileSystems will return
     * a 404 "FileSystemNotFound" error.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DeleteFileSystem</code> action.
     * </p>
     *
     * @param deleteFileSystemRequest Container for the necessary parameters
     *           to execute the DeleteFileSystem operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DeleteFileSystem service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> deleteFileSystemAsync(
            final DeleteFileSystemRequest deleteFileSystemRequest,
            final AsyncHandler<DeleteFileSystemRequest, Void> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
              try {
                deleteFileSystem(deleteFileSystemRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(deleteFileSystemRequest, null);
                 return null;
        }
    });
    }
    
    /**
     * <p>
     * Returns the descriptions of the current mount targets for a file
     * system. The order of mount targets returned in the response is
     * unspecified.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DescribeMountTargets</code> action on the file
     * system <code>FileSystemId</code> .
     * </p>
     *
     * @param describeMountTargetsRequest Container for the necessary
     *           parameters to execute the DescribeMountTargets operation on
     *           AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeMountTargets service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeMountTargetsResult> describeMountTargetsAsync(final DescribeMountTargetsRequest describeMountTargetsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeMountTargetsResult>() {
            public DescribeMountTargetsResult call() throws Exception {
                return describeMountTargets(describeMountTargetsRequest);
        }
    });
    }

    /**
     * <p>
     * Returns the descriptions of the current mount targets for a file
     * system. The order of mount targets returned in the response is
     * unspecified.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DescribeMountTargets</code> action on the file
     * system <code>FileSystemId</code> .
     * </p>
     *
     * @param describeMountTargetsRequest Container for the necessary
     *           parameters to execute the DescribeMountTargets operation on
     *           AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeMountTargets service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeMountTargetsResult> describeMountTargetsAsync(
            final DescribeMountTargetsRequest describeMountTargetsRequest,
            final AsyncHandler<DescribeMountTargetsRequest, DescribeMountTargetsResult> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeMountTargetsResult>() {
            public DescribeMountTargetsResult call() throws Exception {
              DescribeMountTargetsResult result;
                try {
                result = describeMountTargets(describeMountTargetsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(describeMountTargetsRequest, result);
                 return result;
        }
    });
    }
    
    /**
     * <p>
     * Creates a mount target for a file system. You can then mount the file
     * system on EC2 instances via the mount target.
     * </p>
     * <p>
     * You can create one mount target in each Availability Zone in your
     * VPC. All EC2 instances in a VPC within a given Availability Zone share
     * a single mount target for a given file system. If you have multiple
     * subnets in an Availability Zone, you create a mount target in one of
     * the subnets. EC2 instances do not need to be in the same subnet as the
     * mount target in order to access their file system. For more
     * information, see
     * <a href="http://docs.aws.amazon.com/efs/latest/ug/how-it-works.html"> Amazon EFS: How it Works </a>
     * .
     * </p>
     * <p>
     * In the request, you also specify a file system ID for which you are
     * creating the mount target and the file system's lifecycle state must
     * be "available" (see DescribeFileSystems).
     * </p>
     * <p>
     * In the request, you also provide a subnet ID, which serves several
     * purposes:
     * </p>
     * 
     * <ul>
     * <li>It determines the VPC in which Amazon EFS creates the mount
     * target.</li>
     * <li>It determines the Availability Zone in which Amazon EFS creates
     * the mount target. </li>
     * <li>It determines the IP address range from which Amazon EFS selects
     * the IP address of the mount target if you don't specify an IP address
     * in the request. </li>
     * 
     * </ul>
     * <p>
     * After creating the mount target, Amazon EFS returns a response that
     * includes, a <code>MountTargetId</code> and an <code>IpAddress</code> .
     * You use this IP address when mounting the file system in an EC2
     * instance. You can also use the mount target's DNS name when mounting
     * the file system. The EC2 instance on which you mount the file system
     * via the mount target can resolve the mount target's DNS name to its IP
     * address. For more information, see
     * <a href="http://docs.aws.amazon.com/efs/latest/ug/how-it-works.html#how-it-works-implementation"> How it Works: Implementation Overview </a>
     * 
     * </p>
     * <p>
     * Note that you can create mount targets for a file system in only one
     * VPC, and there can be only one mount target per Availability Zone.
     * That is, if the file system already has one or more mount targets
     * created for it, the request to add another mount target must meet the
     * following requirements:
     * </p>
     * 
     * <ul>
     * <li> <p>
     * The subnet specified in the request must belong to the same VPC as
     * the subnets of the existing mount targets.
     * </p>
     * </li>
     * <li>The subnet specified in the request must not be in the same
     * Availability Zone as any of the subnets of the existing mount
     * targets.</li>
     * 
     * </ul>
     * <p>
     * If the request satisfies the requirements, Amazon EFS does the
     * following:
     * </p>
     * 
     * <ul>
     * <li>Creates a new mount target in the specified subnet. </li>
     * <li>Also creates a new network interface in the subnet as follows:
     * <ul>
     * <li>If the request provides an <code>IpAddress</code> , Amazon EFS
     * assigns that IP address to the network interface. Otherwise, Amazon
     * EFS assigns a free address in the subnet (in the same way that the
     * Amazon EC2 <code>CreateNetworkInterface</code> call does when a
     * request does not specify a primary private IP address).</li>
     * <li>If the request provides <code>SecurityGroups</code> , this
     * network interface is associated with those security groups. Otherwise,
     * it belongs to the default security group for the subnet's VPC.</li>
     * <li>Assigns the description <code>"Mount target fsmt-id for file
     * system fs-id"</code> where <code> fsmt-id </code> is the mount target
     * ID, and <code> fs-id </code> is the <code>FileSystemId</code> .</li>
     * <li>Sets the <code>requesterManaged</code> property of the network
     * interface to "true", and the <code>requesterId</code> value to
     * "EFS".</li>
     * 
     * </ul>
     * <p>
     * Each Amazon EFS mount target has one corresponding requestor-managed
     * EC2 network interface. After the network interface is created, Amazon
     * EFS sets the <code>NetworkInterfaceId</code> field in the mount
     * target's description to the network interface ID, and the
     * <code>IpAddress</code> field to its address. If network interface
     * creation fails, the entire <code>CreateMountTarget</code> operation
     * fails.
     * </p>
     * </li>
     * 
     * </ul>
     * <p>
     * <b>NOTE:</b>The CreateMountTarget call returns only after creating
     * the network interface, but while the mount target state is still
     * "creating". You can check the mount target creation status by calling
     * the DescribeFileSystems API, which among other things returns the
     * mount target state.
     * </p>
     * <p>
     * We recommend you create a mount target in each of the Availability
     * Zones. There are cost considerations for using a file system in an
     * Availability Zone through a mount target created in another
     * Availability Zone. For more information, go to
     * <a href="http://aws.amazon.com/efs/"> Amazon EFS </a>
     * product detail page. In addition, by always using a mount target
     * local to the instance's Availability Zone, you eliminate a partial
     * failure scenario; if the Availablity Zone in which your mount target
     * is created goes down, then you won't be able to access your file
     * system through that mount target.
     * </p>
     * <p>
     * This operation requires permission for the following action on the
     * file system:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:CreateMountTarget</code> </li>
     * 
     * </ul>
     * <p>
     * This operation also requires permission for the following Amazon EC2
     * actions:
     * </p>
     * 
     * <ul>
     * <li> <code>ec2:DescribeSubnets</code> </li>
     * <li> <code>ec2:DescribeNetworkInterfaces</code> </li>
     * <li> <code>ec2:CreateNetworkInterface</code> </li>
     * 
     * </ul>
     *
     * @param createMountTargetRequest Container for the necessary parameters
     *           to execute the CreateMountTarget operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         CreateMountTarget service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<CreateMountTargetResult> createMountTargetAsync(final CreateMountTargetRequest createMountTargetRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<CreateMountTargetResult>() {
            public CreateMountTargetResult call() throws Exception {
                return createMountTarget(createMountTargetRequest);
        }
    });
    }

    /**
     * <p>
     * Creates a mount target for a file system. You can then mount the file
     * system on EC2 instances via the mount target.
     * </p>
     * <p>
     * You can create one mount target in each Availability Zone in your
     * VPC. All EC2 instances in a VPC within a given Availability Zone share
     * a single mount target for a given file system. If you have multiple
     * subnets in an Availability Zone, you create a mount target in one of
     * the subnets. EC2 instances do not need to be in the same subnet as the
     * mount target in order to access their file system. For more
     * information, see
     * <a href="http://docs.aws.amazon.com/efs/latest/ug/how-it-works.html"> Amazon EFS: How it Works </a>
     * .
     * </p>
     * <p>
     * In the request, you also specify a file system ID for which you are
     * creating the mount target and the file system's lifecycle state must
     * be "available" (see DescribeFileSystems).
     * </p>
     * <p>
     * In the request, you also provide a subnet ID, which serves several
     * purposes:
     * </p>
     * 
     * <ul>
     * <li>It determines the VPC in which Amazon EFS creates the mount
     * target.</li>
     * <li>It determines the Availability Zone in which Amazon EFS creates
     * the mount target. </li>
     * <li>It determines the IP address range from which Amazon EFS selects
     * the IP address of the mount target if you don't specify an IP address
     * in the request. </li>
     * 
     * </ul>
     * <p>
     * After creating the mount target, Amazon EFS returns a response that
     * includes, a <code>MountTargetId</code> and an <code>IpAddress</code> .
     * You use this IP address when mounting the file system in an EC2
     * instance. You can also use the mount target's DNS name when mounting
     * the file system. The EC2 instance on which you mount the file system
     * via the mount target can resolve the mount target's DNS name to its IP
     * address. For more information, see
     * <a href="http://docs.aws.amazon.com/efs/latest/ug/how-it-works.html#how-it-works-implementation"> How it Works: Implementation Overview </a>
     * 
     * </p>
     * <p>
     * Note that you can create mount targets for a file system in only one
     * VPC, and there can be only one mount target per Availability Zone.
     * That is, if the file system already has one or more mount targets
     * created for it, the request to add another mount target must meet the
     * following requirements:
     * </p>
     * 
     * <ul>
     * <li> <p>
     * The subnet specified in the request must belong to the same VPC as
     * the subnets of the existing mount targets.
     * </p>
     * </li>
     * <li>The subnet specified in the request must not be in the same
     * Availability Zone as any of the subnets of the existing mount
     * targets.</li>
     * 
     * </ul>
     * <p>
     * If the request satisfies the requirements, Amazon EFS does the
     * following:
     * </p>
     * 
     * <ul>
     * <li>Creates a new mount target in the specified subnet. </li>
     * <li>Also creates a new network interface in the subnet as follows:
     * <ul>
     * <li>If the request provides an <code>IpAddress</code> , Amazon EFS
     * assigns that IP address to the network interface. Otherwise, Amazon
     * EFS assigns a free address in the subnet (in the same way that the
     * Amazon EC2 <code>CreateNetworkInterface</code> call does when a
     * request does not specify a primary private IP address).</li>
     * <li>If the request provides <code>SecurityGroups</code> , this
     * network interface is associated with those security groups. Otherwise,
     * it belongs to the default security group for the subnet's VPC.</li>
     * <li>Assigns the description <code>"Mount target fsmt-id for file
     * system fs-id"</code> where <code> fsmt-id </code> is the mount target
     * ID, and <code> fs-id </code> is the <code>FileSystemId</code> .</li>
     * <li>Sets the <code>requesterManaged</code> property of the network
     * interface to "true", and the <code>requesterId</code> value to
     * "EFS".</li>
     * 
     * </ul>
     * <p>
     * Each Amazon EFS mount target has one corresponding requestor-managed
     * EC2 network interface. After the network interface is created, Amazon
     * EFS sets the <code>NetworkInterfaceId</code> field in the mount
     * target's description to the network interface ID, and the
     * <code>IpAddress</code> field to its address. If network interface
     * creation fails, the entire <code>CreateMountTarget</code> operation
     * fails.
     * </p>
     * </li>
     * 
     * </ul>
     * <p>
     * <b>NOTE:</b>The CreateMountTarget call returns only after creating
     * the network interface, but while the mount target state is still
     * "creating". You can check the mount target creation status by calling
     * the DescribeFileSystems API, which among other things returns the
     * mount target state.
     * </p>
     * <p>
     * We recommend you create a mount target in each of the Availability
     * Zones. There are cost considerations for using a file system in an
     * Availability Zone through a mount target created in another
     * Availability Zone. For more information, go to
     * <a href="http://aws.amazon.com/efs/"> Amazon EFS </a>
     * product detail page. In addition, by always using a mount target
     * local to the instance's Availability Zone, you eliminate a partial
     * failure scenario; if the Availablity Zone in which your mount target
     * is created goes down, then you won't be able to access your file
     * system through that mount target.
     * </p>
     * <p>
     * This operation requires permission for the following action on the
     * file system:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:CreateMountTarget</code> </li>
     * 
     * </ul>
     * <p>
     * This operation also requires permission for the following Amazon EC2
     * actions:
     * </p>
     * 
     * <ul>
     * <li> <code>ec2:DescribeSubnets</code> </li>
     * <li> <code>ec2:DescribeNetworkInterfaces</code> </li>
     * <li> <code>ec2:CreateNetworkInterface</code> </li>
     * 
     * </ul>
     *
     * @param createMountTargetRequest Container for the necessary parameters
     *           to execute the CreateMountTarget operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         CreateMountTarget service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<CreateMountTargetResult> createMountTargetAsync(
            final CreateMountTargetRequest createMountTargetRequest,
            final AsyncHandler<CreateMountTargetRequest, CreateMountTargetResult> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<CreateMountTargetResult>() {
            public CreateMountTargetResult call() throws Exception {
              CreateMountTargetResult result;
                try {
                result = createMountTarget(createMountTargetRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(createMountTargetRequest, result);
                 return result;
        }
    });
    }
    
    /**
     * <p>
     * Returns the security groups currently in effect for a mount target.
     * This operation requires that the network interface of the mount target
     * has been created and the life cycle state of the mount target is not
     * "deleted".
     * </p>
     * <p>
     * This operation requires permissions for the following actions:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:DescribeMountTargetSecurityGroups</code>
     * action on the mount target's file system. </li>
     * <li> <code>ec2:DescribeNetworkInterfaceAttribute</code> action on the
     * mount target's network interface. </li>
     * 
     * </ul>
     *
     * @param describeMountTargetSecurityGroupsRequest Container for the
     *           necessary parameters to execute the DescribeMountTargetSecurityGroups
     *           operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeMountTargetSecurityGroups service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeMountTargetSecurityGroupsResult> describeMountTargetSecurityGroupsAsync(final DescribeMountTargetSecurityGroupsRequest describeMountTargetSecurityGroupsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeMountTargetSecurityGroupsResult>() {
            public DescribeMountTargetSecurityGroupsResult call() throws Exception {
                return describeMountTargetSecurityGroups(describeMountTargetSecurityGroupsRequest);
        }
    });
    }

    /**
     * <p>
     * Returns the security groups currently in effect for a mount target.
     * This operation requires that the network interface of the mount target
     * has been created and the life cycle state of the mount target is not
     * "deleted".
     * </p>
     * <p>
     * This operation requires permissions for the following actions:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:DescribeMountTargetSecurityGroups</code>
     * action on the mount target's file system. </li>
     * <li> <code>ec2:DescribeNetworkInterfaceAttribute</code> action on the
     * mount target's network interface. </li>
     * 
     * </ul>
     *
     * @param describeMountTargetSecurityGroupsRequest Container for the
     *           necessary parameters to execute the DescribeMountTargetSecurityGroups
     *           operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeMountTargetSecurityGroups service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeMountTargetSecurityGroupsResult> describeMountTargetSecurityGroupsAsync(
            final DescribeMountTargetSecurityGroupsRequest describeMountTargetSecurityGroupsRequest,
            final AsyncHandler<DescribeMountTargetSecurityGroupsRequest, DescribeMountTargetSecurityGroupsResult> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeMountTargetSecurityGroupsResult>() {
            public DescribeMountTargetSecurityGroupsResult call() throws Exception {
              DescribeMountTargetSecurityGroupsResult result;
                try {
                result = describeMountTargetSecurityGroups(describeMountTargetSecurityGroupsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(describeMountTargetSecurityGroupsRequest, result);
                 return result;
        }
    });
    }
    
    /**
     * <p>
     * Modifies the set of security groups in effect for a mount target.
     * </p>
     * <p>
     * When you create a mount target, Amazon EFS also creates a new network
     * interface (see CreateMountTarget). This operation replaces the
     * security groups in effect for the network interface associated with a
     * mount target, with the <code>SecurityGroups</code> provided in the
     * request. This operation requires that the network interface of the
     * mount target has been created and the life cycle state of the mount
     * target is not "deleted".
     * </p>
     * <p>
     * The operation requires permissions for the following actions:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:ModifyMountTargetSecurityGroups</code>
     * action on the mount target's file system. </li>
     * <li> <code>ec2:ModifyNetworkInterfaceAttribute</code> action on the
     * mount target's network interface. </li>
     * 
     * </ul>
     *
     * @param modifyMountTargetSecurityGroupsRequest Container for the
     *           necessary parameters to execute the ModifyMountTargetSecurityGroups
     *           operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         ModifyMountTargetSecurityGroups service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> modifyMountTargetSecurityGroupsAsync(final ModifyMountTargetSecurityGroupsRequest modifyMountTargetSecurityGroupsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
                modifyMountTargetSecurityGroups(modifyMountTargetSecurityGroupsRequest);
                return null;
        }
    });
    }

    /**
     * <p>
     * Modifies the set of security groups in effect for a mount target.
     * </p>
     * <p>
     * When you create a mount target, Amazon EFS also creates a new network
     * interface (see CreateMountTarget). This operation replaces the
     * security groups in effect for the network interface associated with a
     * mount target, with the <code>SecurityGroups</code> provided in the
     * request. This operation requires that the network interface of the
     * mount target has been created and the life cycle state of the mount
     * target is not "deleted".
     * </p>
     * <p>
     * The operation requires permissions for the following actions:
     * </p>
     * 
     * <ul>
     * <li> <code>elasticfilesystem:ModifyMountTargetSecurityGroups</code>
     * action on the mount target's file system. </li>
     * <li> <code>ec2:ModifyNetworkInterfaceAttribute</code> action on the
     * mount target's network interface. </li>
     * 
     * </ul>
     *
     * @param modifyMountTargetSecurityGroupsRequest Container for the
     *           necessary parameters to execute the ModifyMountTargetSecurityGroups
     *           operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         ModifyMountTargetSecurityGroups service method, as returned by
     *         AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> modifyMountTargetSecurityGroupsAsync(
            final ModifyMountTargetSecurityGroupsRequest modifyMountTargetSecurityGroupsRequest,
            final AsyncHandler<ModifyMountTargetSecurityGroupsRequest, Void> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
              try {
                modifyMountTargetSecurityGroups(modifyMountTargetSecurityGroupsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(modifyMountTargetSecurityGroupsRequest, null);
                 return null;
        }
    });
    }
    
    /**
     * <p>
     * Returns the tags associated with a file system. The order of tags
     * returned in the response of one <code>DescribeTags</code> call, and
     * the order of tags returned across the responses of a multi-call
     * iteration (when using pagination), is unspecified.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DescribeTags</code> action.
     * </p>
     *
     * @param describeTagsRequest Container for the necessary parameters to
     *           execute the DescribeTags operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeTags service method, as returned by AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeTagsResult> describeTagsAsync(final DescribeTagsRequest describeTagsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeTagsResult>() {
            public DescribeTagsResult call() throws Exception {
                return describeTags(describeTagsRequest);
        }
    });
    }

    /**
     * <p>
     * Returns the tags associated with a file system. The order of tags
     * returned in the response of one <code>DescribeTags</code> call, and
     * the order of tags returned across the responses of a multi-call
     * iteration (when using pagination), is unspecified.
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DescribeTags</code> action.
     * </p>
     *
     * @param describeTagsRequest Container for the necessary parameters to
     *           execute the DescribeTags operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DescribeTags service method, as returned by AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<DescribeTagsResult> describeTagsAsync(
            final DescribeTagsRequest describeTagsRequest,
            final AsyncHandler<DescribeTagsRequest, DescribeTagsResult> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<DescribeTagsResult>() {
            public DescribeTagsResult call() throws Exception {
              DescribeTagsResult result;
                try {
                result = describeTags(describeTagsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(describeTagsRequest, result);
                 return result;
        }
    });
    }
    
    /**
     * <p>
     * Deletes the specified tags from a file system. If the
     * <code>DeleteTags</code> request includes a tag key that does not
     * exist, Amazon EFS ignores it; it is not an error. For more information
     * about tags and related restrictions, go to
     * <a href="http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/cost-alloc-tags.html"> Tag Restrictions </a>
     * in the <i>AWS Billing and Cost Management User Guide</i> .
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DeleteTags</code> action.
     * </p>
     *
     * @param deleteTagsRequest Container for the necessary parameters to
     *           execute the DeleteTags operation on AmazonElasticFileSystem.
     * 
     * @return A Java Future object containing the response from the
     *         DeleteTags service method, as returned by AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> deleteTagsAsync(final DeleteTagsRequest deleteTagsRequest) 
            throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
                deleteTags(deleteTagsRequest);
                return null;
        }
    });
    }

    /**
     * <p>
     * Deletes the specified tags from a file system. If the
     * <code>DeleteTags</code> request includes a tag key that does not
     * exist, Amazon EFS ignores it; it is not an error. For more information
     * about tags and related restrictions, go to
     * <a href="http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/cost-alloc-tags.html"> Tag Restrictions </a>
     * in the <i>AWS Billing and Cost Management User Guide</i> .
     * </p>
     * <p>
     * This operation requires permission for the
     * <code>elasticfilesystem:DeleteTags</code> action.
     * </p>
     *
     * @param deleteTagsRequest Container for the necessary parameters to
     *           execute the DeleteTags operation on AmazonElasticFileSystem.
     * @param asyncHandler Asynchronous callback handler for events in the
     *           life-cycle of the request. Users could provide the implementation of
     *           the four callback methods in this interface to process the operation
     *           result or handle the exception.
     * 
     * @return A Java Future object containing the response from the
     *         DeleteTags service method, as returned by AmazonElasticFileSystem.
     * 
     *
     * @throws AmazonClientException
     *             If any internal errors are encountered inside the client while
     *             attempting to make the request or handle the response.  For example
     *             if a network connection is not available.
     * @throws AmazonServiceException
     *             If an error response is returned by AmazonElasticFileSystem indicating
     *             either a problem with the data in the request, or a server side issue.
     */
    public Future<Void> deleteTagsAsync(
            final DeleteTagsRequest deleteTagsRequest,
            final AsyncHandler<DeleteTagsRequest, Void> asyncHandler)
                    throws AmazonServiceException, AmazonClientException {
        return executorService.submit(new Callable<Void>() {
            public Void call() throws Exception {
              try {
                deleteTags(deleteTagsRequest);
              } catch (Exception ex) {
                  asyncHandler.onError(ex);
            throw ex;
              }
              asyncHandler.onSuccess(deleteTagsRequest, null);
                 return null;
        }
    });
    }
    
}
        