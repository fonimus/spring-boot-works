var app = angular.module("webSocketApp", [
    "webSocketApp.controllers",
    "webSocketApp.services",
    "toastr",
    "ui.router",
    "ui.bootstrap",
    "chart.js"
]);

angular.module("webSocketApp.controllers", []);
angular.module("webSocketApp.services", []);

app.controller("WebSocketCtrl", function ($scope, $state) {
    $scope.messages = [];
    $scope.new = {};

    $scope.tabs = [
        {'state': 'Home', 'name': 'Home'},
        {'state': 'Health', 'name': 'Health'},
        {'state': 'Metrics', 'name': 'Metrics'},
        {'state': 'Loggers', 'name': 'Loggers'}
    ];

    $scope.current = function () {
        return $state.current.name;
    }

    $scope.loading = false;

    $scope.loadStart = function () {
        $scope.loading = true;
    };

    $scope.loadEnd = function () {
        $scope.loading = false;
    };
});

app.config(function ($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('Home', {
            url: '/',
            controller: 'HomeCtrl as home',
            templateUrl: 'views/home.html',
            name: 'Home',
            title: 'Home'
        })
        .state('Health', {
            url: '/health',
            controller: 'HealthCtrl as health',
            templateUrl: 'views/health.html',
            name: 'Health',
            title: 'Health'
        })
        .state('Metrics', {
            url: '/metrics',
            controller: 'MetricsCtrl as metrics',
            templateUrl: 'views/metrics.html',
            name: 'Metrics',
            title: 'Metrics'
        })
        .state('Loggers', {
            url: '/loggers',
            controller: 'LoggersCtrl as loggers',
            templateUrl: 'views/loggers.html',
            name: 'Loggers',
            title: 'Loggers'
        });

    $urlRouterProvider.otherwise('/');
});

app.controller("HomeCtrl", function ($scope, $http, _websocket, toastr) {

    $scope.author = "francois";

    $scope.loadStart();
    _websocket.listTopics().then(function (result) {
        $scope.topics = result.data;
        $scope.loadEnd();
    }, function (error) {
        console.log(error);
        $scope.loadEnd();
    });

    $scope.addMessage = function () {
        _websocket.addTopic($scope.new.topic, $scope.author, $scope.new.message).then(function (result) {
            $scope.new = {};
        }, function (error) {
            console.log(error);
            $scope.loadEnd();
        });
    };

    $scope.refresh = function (topic) {
        _websocket.refreshTopic(topic);
    }

    _websocket.receive().then(null, null, function (notification) {
        if (notification.type == 'update') {
            toastr.success('Updates available', 'Topic ' + notification.resourceName);
            if (notification.message) {
                for (var i in $scope.topics) {
                    if ($scope.topics[i].id === notification.resourceId) {
                        console.log('Update topic : ' + notification.resourceName);
                        $scope.topics[i].messages.push(notification.message);
                    }
                }
            }
        } else if (notification.type == 'same') {
            toastr.info('No updates', 'Topic ' + notification.resourceName);
        } else if (notification.type == 'disconnect') {
            toastr.error('session timeout', 'Error');
        }
        $scope.messages.push(notification);
    });
});

app.controller("HealthCtrl", function ($scope, $http, toastr) {

    var getClass = function (status) {
        if (status !== 'UP') {
            return 'label-error';
        }
        return 'label-success';
    };

    $scope.getProgressBarType = function (total, free, threshold) {
        if ((total - free) < threshold) {
            return 'danger';
        }
        return 'success';
    };

    $scope.refresh = function (notification) {

        $scope.healths = [];

        $scope.loadStart();
        $http.get("/manage/health").then(function (result) {
            for (var prop in result.data) {
                if (prop === 'status') {
                    var status = result.data[prop];
                    $scope.healths.push({'name': 'General', 'status': status, 'class': getClass(status)});
                } else {
                    var status = result.data[prop].status;
                    var health = {'name': prop, 'status': status, 'class': getClass(status)};
                    for (var i in result.data[prop]) {
                        health[i] = result.data[prop][i];
                    }
                    console.log(health)
                    $scope.healths.push(health);
                }
            }
            $scope.loadEnd();
            if (notification) {
                toastr.success('Data updated');
            }
        }, function (error) {
            console.log(error);
            $scope.loadEnd();
        });
    };
    $scope.refresh();
});

app.controller("LoggersCtrl", function ($scope, $http, toastr) {

    var getClass = function (level) {
        if (level === 'INFO') {
            return 'label-info';
        }
        if (level === 'WARN') {
            return 'label-warning';
        }
        if (level === 'ERROR') {
            return 'label-danger';
        }
        if (level === 'DEBUG') {
            return 'label-success';
        }
        if (level === 'TRACE') {
            return 'label-primary';
        }
        return 'label-default';
    };

    $scope.refresh = function (notification) {

        $scope.logs = [];
        $scope.levels = [];

        $scope.loadStart();
        $http.get("/manage/loggers").then(function (result) {
            $scope.levels = result.data.levels;
            for (var prop in result.data.loggers) {
                var logger = {
                    'name': prop,
                    'effectiveLevel': result.data.loggers[prop].effectiveLevel,
                    'configuredLevel': result.data.loggers[prop].configuredLevel
                };
                $scope.logs.push(logger);
            }
            $scope.loadEnd();
            if (notification) {
                toastr.success('Data updated');
            }
        }, function (error) {
            console.log(error);
            $scope.loadEnd();
        });
    };

    $scope.updateLevel = function (logger, level) {
        console.log('Update logger name "' + logger.name + '" with level "' + level + '"')
        var body;
        if (level === 'INHERIT') {
            body = {};
        } else {
            body = {"configuredLevel": level};
        }
        $scope.loadStart();
        $http.post("/manage/loggers/" + logger.name, body).then(function (result) {
            $scope.refresh();
        }, function (error) {
            console.log(error);
            $scope.loadEnd();
        });
    };

    $scope.getClass = function (level) {
        return getClass(level);
    };

    $scope.getClasses = function (level) {
        var result = [];
        result.push(getClass(level));
        return result;
    };

    $scope.refresh();
});

app.controller("MetricsCtrl", function ($scope, $http, toastr, $q, $filter) {

    $scope.statusCodes = ['all', '200', '400', '404', '500'];

    $scope.xFunction = function () {
        return function (d) {
            return d.key;
        };
    };

    $scope.yFunction = function () {
        return function (d) {
            return d.y;
        };
    };

    $scope.descriptionFunction = function () {
        return function (d) {
            return d.key;
        }
    };

    $scope.refresh = function (notification, skipMetrics, skipThreads, skipGraphs) {

        $scope.threadsOptions = {
            title: {display: false},
            legend: {display: true, position: 'bottom'}
        };
        $scope.series = ['Mean', 'Min', 'Max'];

        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };

        if (!skipMetrics) {
            $scope.metrics = [];
            $scope.metricsHttp = {};
            $scope.metricsServices = {};

            $scope.loadingMetrics = true;
            $http.get("/manage/metrics").then(function (result) {
                for (var s in $scope.statusCodes) {
                    $scope.metricsHttp[$scope.statusCodes[s]] = 0;
                }
                $scope.metrics = result.data;
                for (var name in result.data) {
                    if (name.indexOf('counter.status.') === 0) {
                        for (var i in $scope.statusCodes) {
                            var status = $scope.statusCodes[i];
                            if (name.indexOf('counter.status.' + status + '.api') === 0) {
                                $scope.metricsHttp[status] = $scope.metricsHttp[status] + result.data[name];
                                $scope.metricsHttp['all'] = $scope.metricsHttp['all'] + result.data[name];
                            }
                        }
                    } else if (name.indexOf('fonimus.') === 0) {
                        if (name.indexOf('count') > 0) {
                            var serviceName = name.substring(0, name.indexOf('count') - 1);
                            if (!$scope.metricsServices[serviceName]) {
                                $scope.metricsServices[serviceName] = {};
                            }
                            $scope.metricsServices[serviceName].count = result.data[name];
                        } else if (name.indexOf('snapshot') > 0) {
                            var serviceName = name.substring(0, name.indexOf('snapshot') - 1);
                            if (!$scope.metricsServices[serviceName]) {
                                $scope.metricsServices[serviceName] = {};
                            }
                            var type = name.substring(name.indexOf('snapshot') + 'snapshot'.length + 1);
                            $scope.metricsServices[serviceName][type] = result.data[name];
                        }
                    }
                }
                $scope.loadingMetrics = false;
            }, function (error) {
                console.log(error);
                $scope.loadingMetrics = false;
            });
        }

        if (!skipThreads) {
            $scope.loadingThreads = true;
            $scope.threads = {};
            $scope.threadsData = [];

            $scope.threadsLabels = [];
            $scope.threadsData = [];
            $http.get("/manage/dump").then(function (result) {
                $scope.threads['all'] = 0;
                for (var i in result.data) {
                    var state = result.data[i].threadState;
                    if (!$scope.threads[state]) {
                        $scope.threads[state] = 0;
                    }
                    $scope.threads[state]++;
                    $scope.threads['all']++;
                }
                for (var state in $scope.threads) {
                    if (state !== 'all') {
                        $scope.threadsLabels.push(state);
                        $scope.threadsData.push($scope.threads[state]);
                    }
                }
                $scope.loadingThreads = false;
            }, function (error) {
                console.log(error);
                $scope.loadingThreads = false;
            });
        }

        if (!skipGraphs) {
            $scope.loadingGraphs = true;
            $scope.graphs = {};
            var requests = [];
            $http.get("/api/metrics?limit=500").then(function (result) {
                for (var i in result.data) {
                    var name = result.data[i].name;
                    if (!$scope.graphs[name]) {
                        $scope.graphs[name] = {};
                        $scope.graphs[name].labels = [];
                        $scope.graphs[name].data = [];
                        $scope.graphs[name].options = {
                            title: {display: true, text: name},
                            legend: {display: true, position: 'bottom'}
                        };
                        $scope.graphs[name].data[0] = [];
                        $scope.graphs[name].data[1] = [];
                        $scope.graphs[name].data[2] = [];
                    }
                    $scope.graphs[name].labels.push($filter('date')(result.data[i].time, 'yyyy-MM-dd HH:mm:ss'));
                    $scope.graphs[name].data[0].push($filter('number')(result.data[i].mean, 0));
                    $scope.graphs[name].data[1].push($filter('number')(result.data[i].min, 0));
                    $scope.graphs[name].data[2].push($filter('number')(result.data[i].max, 0));
                }
                console.log($scope.graphs)
                $scope.loadingGraphs = false;
            }, function (error) {
                console.log(error);
                $scope.loadingGraphs = false;
            });
        }

    };
    $scope.refresh();
});

app.directive("spinner", function () {
    return {
        templateUrl: 'views/spinner.html'
    };
});

app.service("_websocket", function ($q, $timeout, $http) {

    var service = {}, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    };

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/websocket";
    service.NOTIFICATION_TOPIC = "/topic/notification";
    service.HELLO_BROKER = "/app/refresh";

    service.listTopics = function () {
        return $http.get("/api/topic");
    }

    service.addTopic = function (topicId, author, content) {
        return $http.put("/api/topic/" + topicId, {
            content: content,
            author: author,
            time: Date.now()
        });
    }

    service.receive = function () {
        return listener.promise;
    };

    service.refreshTopic = function (topic) {
        socket.stomp.send(service.HELLO_BROKER, {
            priority: 9
        }, JSON.stringify({
            topicId: topic.id,
            lastUpdate: topic.lastUpdate
        }));
    };

    var reconnect = function () {
        $timeout(function () {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var startListener = function () {
        socket.stomp.subscribe(service.NOTIFICATION_TOPIC, function (data) {
            listener.notify(JSON.parse(data.body));
        });
    };

    var initialize = function () {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();

    return service;
});

app.filter('size', function () {
    return function (bytes, precision) {
        if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) return '-';
        if (typeof precision === 'undefined') precision = 1;
        var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
            number = Math.floor(Math.log(bytes) / Math.log(1024));
        return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + ' ' + units[number];
    }
});

app.filter('msToStr', function () {
    return function (millseconds) {
        var oneSecond = 1000;
        var oneMinute = oneSecond * 60;
        var oneHour = oneMinute * 60;
        var oneDay = oneHour * 24;

        var seconds = Math.floor((millseconds % oneMinute) / oneSecond);
        var minutes = Math.floor((millseconds % oneHour) / oneMinute);
        var hours = Math.floor((millseconds % oneDay) / oneHour);
        var days = Math.floor(millseconds / oneDay);

        var timeString = '';
        if (days !== 0) {
            timeString += (days !== 1) ? (days + ' days ') : (days + ' day ');
        }
        if (hours !== 0) {
            timeString += (hours !== 1) ? (hours + ' hours ') : (hours + ' hour ');
        }
        if (minutes !== 0) {
            timeString += (minutes !== 1) ? (minutes + ' minutes ') : (minutes + ' minute ');
        }
        if (seconds !== 0 || millseconds < 1000) {
            timeString += (seconds !== 1) ? (seconds + ' seconds ') : (seconds + ' second ');
        }

        return timeString;
    };
});