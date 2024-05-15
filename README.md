# SubServer

## About SubServer

SubServer is a 1.12.2 Minecraft plugin made with Spigot. The plugin splits your server into instances. An instance can have multiples maps and players, like a sub-server. It uses Slime World Manager to asynchronously generate maps.

## Depend

```https://github.com/cijaaimee/Slime-World-Manager```

### Using SubServer

SubServer uses a templating system which works by registering your `InstanceType` (your template) inside an `InstanceFactory`. You have a few settings to configure inside the `InstanceType` object and most importantly, you need to set a runnable that will be executed after the initialization of each instance.
That is your entrypoint to control the behavior of instances.

Another important point is that for each instance, if you need to register an event listener, you must do it using the `Instance#registerListener` method. It will allow you to only catch the events of your own instance, you won't have to filter yourself which event is yours.

***
## Contributors
- [LoanSpac](https://github.com/LoanSpac)
- [Clooooud](https://github.com/Clooooud)
